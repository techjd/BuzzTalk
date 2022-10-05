package com.ssip.buzztalk.ui.fragments.videocall

import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssip.buzztalk.databinding.FragmentVideoCallBinding
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import org.webrtc.*
import java.net.URISyntaxException
import javax.inject.Inject


@AndroidEntryPoint
class VideoCallFragment : Fragment() {

    val TAG = "Video Call Fragment"

    private var _binding: FragmentVideoCallBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var socket: Socket

    private var isMuted = false
    private var peerConnection: PeerConnection? = null
    private var rootEglBase: EglBase? = null
    private var factory: PeerConnectionFactory? = null
    private var videoTrackFromCamera: VideoTrack? = null
    private var audioTrackFromLocal: AudioTrack? = null
    private var videoCapturer: VideoCapturer? = null

    private val args: VideoCallFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val audioManager = requireContext().getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager
        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        audioManager.isSpeakerphoneOn = true

        runBlocking {
            connectToSignallingServer()
            initializeSurfaceViews()


            initializePeerConnectionFactory();
            createVideoTrackFromCameraAndShowIt();

            initializePeerConnections();
            startStreamingVideo();
        }
    }

    private fun startStreamingVideo() {
        val mediaStream = factory!!.createLocalMediaStream("ARDAMS")
        mediaStream.addTrack(videoTrackFromCamera)
        mediaStream.addTrack(audioTrackFromLocal)
        peerConnection!!.addStream(mediaStream)
    }

    private fun initializePeerConnections() {
        peerConnection = createPeerConnection(factory!!)
    }

    private fun createPeerConnection(factory: PeerConnectionFactory): PeerConnection? {
        val iceServers: ArrayList<PeerConnection.IceServer> = ArrayList()
        iceServers.add(PeerConnection.IceServer("stun:stun.l.google.com:19302"))
        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
        val pcConstraints = MediaConstraints()

        val pcObserver: PeerConnection.Observer = object : PeerConnection.Observer {
            override fun onSignalingChange(signalingState: PeerConnection.SignalingState) {
                Log.d(TAG, "onSignalingChange: ")
            }

            override fun onIceConnectionChange(iceConnectionState: PeerConnection.IceConnectionState) {
                Log.d(TAG, "onIceConnectionChange: ")
//                binding!!.remoteStream.setEnabled(false)
//                binding!!.remoteStream.release()
            }

            override fun onIceConnectionReceivingChange(b: Boolean) {
                Log.d(TAG, "onIceConnectionReceivingChange: ")
            }

            override fun onIceGatheringChange(iceGatheringState: PeerConnection.IceGatheringState) {
                Log.d(TAG, "onIceGatheringChange: ")
            }

            override fun onIceCandidate(iceCandidate: IceCandidate) {
                Log.d(TAG, "onIceCandidate: ")
                val message = JSONObject()
                try {
                    message.put("type", "candidate")
                    message.put("label", iceCandidate.sdpMLineIndex)
                    message.put("id", iceCandidate.sdpMid)
                    message.put("conversationId", args.conversationId)
                    message.put("candidate", iceCandidate.sdp)
                    Log.d(TAG, "onIceCandidate: sending candidate $message")
                    socket.emit("ice", message)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onIceCandidatesRemoved(iceCandidates: Array<IceCandidate>) {
                Log.d(TAG, "onIceCandidatesRemoved: ")
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onAddStream(mediaStream: MediaStream) {
                Log.d(TAG, "onAddStream: " + mediaStream.videoTracks.size)
                if (binding.remoteStream.isActivated)
                    mediaStream.videoTracks.first.addRenderer(VideoRenderer(binding!!.remoteStream))
            }

            override fun onRemoveStream(mediaStream: MediaStream) {
                Log.d(TAG, "onRemoveStream: ")
            }

            override fun onDataChannel(dataChannel: DataChannel) {
                Log.d(TAG, "onDataChannel: ")
            }

            override fun onRenegotiationNeeded() {
                Log.d(TAG, "onRenegotiationNeeded: ")
            }
        }
        return factory.createPeerConnection(rtcConfig, pcConstraints, pcObserver)
    }

    private fun createVideoTrackFromCameraAndShowIt() {
        videoCapturer = createVideoCapturer()
        val videoSource = factory!!.createVideoSource(videoCapturer)
        videoCapturer!!.startCapture(1280, 720, 30)
        videoTrackFromCamera = factory!!.createVideoTrack("ARDAMSv0", videoSource)
        videoTrackFromCamera!!.setEnabled(true)


        val audioSource = factory!!.createAudioSource(MediaConstraints())
        audioTrackFromLocal = factory!!.createAudioTrack("ARDAMSa0", audioSource)
        audioTrackFromLocal!!.setEnabled(true)
        isMuted = false
        videoTrackFromCamera!!.addRenderer(VideoRenderer(binding!!.localStream))
    }

    private fun initializePeerConnectionFactory() {
        PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true);
        factory = PeerConnectionFactory(null);
        factory!!.setVideoHwAccelerationOptions(
            rootEglBase!!.eglBaseContext,
            rootEglBase!!.eglBaseContext
        );
    }

    private fun initializeSurfaceViews() {
        rootEglBase = EglBase.create()
        binding.localStream.init(rootEglBase!!.getEglBaseContext(), null)
        binding.localStream.setEnableHardwareScaler(true)
        binding.localStream.setMirror(true)
        binding.remoteStream.init(rootEglBase!!.getEglBaseContext(), null)
        binding.remoteStream.setEnableHardwareScaler(true)
        binding.remoteStream.setMirror(true)
    }

    private fun connectToSignallingServer() {
        try {
            socket.on("offer") { args: Array<Any> ->

                val message = args[0] as JSONObject

                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        binding.receive.setOnClickListener {
                            // We will accept the call
                            doAnswer()
                            it.visibility = View.GONE
                        }
                        binding.call.visibility = View.GONE
                    }
                }

                peerConnection!!.setRemoteDescription(
                    SimpleSdpObserver(),
                    SessionDescription(
                        SessionDescription.Type.OFFER,
                        message.getString("sdp")
                    )
                )
            }.on("answer") { args: Array<Any> ->

                val message = args[0] as JSONObject

                peerConnection!!.setRemoteDescription(
                    SimpleSdpObserver(),
                    SessionDescription(
                        SessionDescription.Type.ANSWER,
                        message.getString("sdp")
                    )
                )

                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        binding.call.visibility = View.GONE
                        binding.receive.visibility = View.GONE
                    }
                }

            }.on("ice") { args: Array<Any> ->

                val message = args[0] as JSONObject

                val candidate = IceCandidate(
                    message.getString("id"),
                    message.getInt("label"),
                    message.getString("candidate")
                )

                peerConnection!!.addIceCandidate(candidate)
            }.on("out") {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Main) {
                        peerConnection!!.close()
                        socket!!.disconnect()
                        Toast.makeText(context, "Call Ended", Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
            }

            socket.connect()

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    private fun doAnswer() {
        peerConnection!!.createAnswer(object : SimpleSdpObserver() {
            override fun onCreateSuccess(p0: SessionDescription?) {
                peerConnection!!.setLocalDescription(SimpleSdpObserver(), p0)
                val message = JSONObject()
                try {
                    message.put("type", "answer")
                    message.put("conversationId", args.conversationId)
                    message.put("sdp", p0!!.description)
                    socket.emit("answer", message)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, MediaConstraints())
    }

    private fun doCall() {
        val sdpMediaConstraints = MediaConstraints()
        peerConnection!!.createOffer(object : SimpleSdpObserver() {
            override fun onCreateSuccess(p0: SessionDescription?) {
                peerConnection!!.setLocalDescription(SimpleSdpObserver(), p0)
                val message = JSONObject()
                try {
                    message.put("type", "offer")
                    message.put("conversationId", args.conversationId)
                    message.put("sdp", p0!!.description)
                    socket.emit("offer", message)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, sdpMediaConstraints)
    }

    private fun createVideoCapturer(): VideoCapturer? {
        val videoCapturer: VideoCapturer?
        videoCapturer = if (useCamera2()) {
            createCameraCapturer(Camera2Enumerator(requireContext()))
        } else {
            createCameraCapturer(Camera1Enumerator(true))
        }
        return videoCapturer
    }

    private fun createCameraCapturer(enumerator: CameraEnumerator): VideoCapturer? {
        val deviceNames = enumerator.deviceNames
        for (deviceName in deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                val videoCapturer: VideoCapturer? = enumerator.createCapturer(deviceName, null)
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }
        for (deviceName in deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                val videoCapturer: VideoCapturer? = enumerator.createCapturer(deviceName, null)
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }
        return null
    }

    private fun useCamera2(): Boolean {
        return Camera2Enumerator.isSupported(requireContext())
    }
}