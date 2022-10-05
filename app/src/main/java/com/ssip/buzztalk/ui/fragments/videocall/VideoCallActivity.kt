package com.ssip.buzztalk.ui.fragments.videocall

import android.media.AudioManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.navArgs
import com.google.gson.Gson
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ActivityVideoCallBinding
import com.ssip.buzztalk.ui.activities.MainActivity
import com.ssip.buzztalk.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import org.webrtc.*
import java.net.URISyntaxException
import javax.inject.Inject

@AndroidEntryPoint
class VideoCallActivity : AppCompatActivity() {

    val TAG = "Video Call Activity"
    lateinit var binding: ActivityVideoCallBinding

    @Inject
    lateinit var socket: Socket

    private var isInitiator = false
    private var isChannelReady = false
    private var isStarted = false
    private var isMuted = false

    private var peerConnection: PeerConnection? = null
    private var rootEglBase: EglBase? = null
    private var factory: PeerConnectionFactory? = null
    private var videoTrackFromCamera: VideoTrack? = null
    private var audioTrackFromLocal: AudioTrack? = null
    private var cameraVideoCapturer: CameraVideoCapturer? = null
    private var videoCapturer: VideoCapturer? = null

    private val args: VideoCallActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val audioManager = this.getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        audioManager.isSpeakerphoneOn = true

        connectToSignallingServer()
        initializeSurfaceViews()


        initializePeerConnectionFactory();
        createVideoTrackFromCameraAndShowIt();

        initializePeerConnections();
        startStreamingVideo();

        binding.endCall.setOnClickListener {
            socket.emit("bye", args.conversationId)

//            peerConnection!!.close()
//            rootEglBase?.release()
            videoTrackFromCamera?.removeRenderer(VideoRenderer(binding.localStream))
//            factory?.dispose()

            finish()
//            socket.disconnect()

//            onBackPressed()
        }

        binding.switchCamera.setOnClickListener {
            switchCamera()
        }

        binding.micOn.setOnClickListener {
            enableAudio()
        }
    }

    fun switchCamera() {
        cameraVideoCapturer = videoCapturer as CameraVideoCapturer
        cameraVideoCapturer!!.switchCamera(null)
    }

    fun enableAudio() {
        if (isMuted) {
            isMuted = false
            audioTrackFromLocal!!.setEnabled(true)
            binding!!.micOn.setImageResource(R.drawable.ic_mic_on)
        } else {
            isMuted = true
            audioTrackFromLocal!!.setEnabled(false)
            binding!!.micOn.setImageResource(R.drawable.ic_mic_off)
        }
    }

    private fun connectToSignallingServer() {
        try {
            socket.emit("create or join", args.conversationId)

            socket.on(
                "created"
            ) { args: Array<Any?>? ->
                Log.d(TAG, "connectToSignallingServer: created")
                isInitiator = true
            }.on("full") { args: Array<Any?>? ->
                Log.d(
                    TAG,
                    "connectToSignallingServer: full"
                )
            }.on(
                "join"
            ) { args: Array<Any?>? ->
                Log.d(TAG, "connectToSignallingServer: join")
                Log.d(
                    TAG,
                    "connectToSignallingServer: Another peer made a request to join room"
                )
                Log.d(
                    TAG,
                    "connectToSignallingServer: This peer is the initiator of room"
                )
                isChannelReady = true
            }.on("joined") { args: Array<Any?>? ->
                Log.d(TAG, "connectToSignallingServer: joined")
                isChannelReady = true
            }.on("log") { args: Array<Any> ->
                for (arg in args) {
                    Log.d(TAG, "connectToSignallingServer: $arg")
                }
            }.on(
                "message"
            ) { args: Array<Any?>? ->
                Log.d(
                    TAG,
                    "connectToSignallingServer: got a message"
                )
            }.on("out") {
                Log.d(TAG, "connectToSignallingServer: Called Bye")
//                peerConnection!!.close()
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Main) {
                        socket.emit("bye1", args.conversationId)
                        Toast.makeText(applicationContext, "Call Ended", Toast.LENGTH_LONG).show()
                        delay(1500L)
//                        peerConnection!!.close()
//                        rootEglBase?.release()
                        videoTrackFromCamera?.removeRenderer(VideoRenderer(binding.localStream))
//                        factory?.dispose()

                        finish()
//                        super.onBackPressed()
                    }
                }
//                binding!!.remoteStream.setEnabled(false)
//                binding!!.remoteStream.release()
//                binding!!.remoteStream
            }.on(
                "message"
            ) { args: Array<Any> ->
                try {
                    val message = args[0] as JSONObject
//                    Log.d(TAG, "connectToSignallingServer: ${message.getString()}")
                    if (message.getString(Constants.USER_MEDIA)  == "got user media") {
//                        val message = args[0] as String
                            maybeStart()
                    } else {
//                        val message = args[0] as JSONObject
                        Log.d(TAG, "connectToSignallingServer: got message $message")
                        if (message.getString("type") == "offer") {
                            Log.d(
                                TAG,
                                "connectToSignallingServer: received an offer $isInitiator $isStarted"
                            )
                            if (!isInitiator && !isStarted) {
                                maybeStart()
                            }
                            peerConnection!!.setRemoteDescription(
                                SimpleSdpObserver(),
                                SessionDescription(
                                    SessionDescription.Type.OFFER,
                                    message.getString("sdp")
                                )
                            )
                            doAnswer()
                        } else if (message.getString("type") == "answer" && isStarted) {
                            peerConnection!!.setRemoteDescription(
                                SimpleSdpObserver(),
                                SessionDescription(
                                    SessionDescription.Type.ANSWER,
                                    message.getString("sdp")
                                )
                            )
                        } else if (message.getString("type") == "candidate" && isStarted) {
                            Log.d(
                                TAG,
                                "connectToSignallingServer: receiving candidates"
                            )
                            val candidate = IceCandidate(
                                message.getString("id"),
                                message.getInt("label"),
                                message.getString("candidate")
                            )
                            peerConnection!!.addIceCandidate(candidate)
                        }
                        /*else if (message === 'bye' && isStarted) {
        handleRemoteHangup();
    }*/
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
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
                    message.put("sdp", p0!!.description)
                    message.put(Constants.USER_MEDIA, "no")
                    message.put(Constants.CONVERSATION_ID, args.conversationId)
                    sendMessage(message)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, MediaConstraints())
    }

    private fun maybeStart() {
        Log.d(TAG, "maybeStart: $isStarted $isChannelReady")
        if (!isStarted && isChannelReady) {
            isStarted = true
            if (isInitiator) {
                doCall()
            }
        }
    }

    private fun doCall() {
        val sdpMediaConstraints = MediaConstraints()
        peerConnection!!.createOffer(object : SimpleSdpObserver() {
            override fun onCreateSuccess(p0: SessionDescription?) {
                Log.d(TAG, "onCreateSuccess: ")
                peerConnection!!.setLocalDescription(SimpleSdpObserver(), p0)
                val message = JSONObject()
                try {
                    message.put("type", "offer")
                    message.put("sdp", p0!!.description)
                    message.put(Constants.USER_MEDIA, "no")
                    message.put(Constants.CONVERSATION_ID, args.conversationId)
                    sendMessage(message)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, sdpMediaConstraints)
    }

    private fun sendMessage(message: JSONObject) {
        socket.emit("message", message)
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

    private fun initializePeerConnectionFactory() {
        PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true);
        factory = PeerConnectionFactory(null);
        factory!!.setVideoHwAccelerationOptions(
            rootEglBase!!.getEglBaseContext(),
            rootEglBase!!.getEglBaseContext()
        );
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

    private fun initializePeerConnections() {
        peerConnection = createPeerConnection(factory!!)
    }

    private fun startStreamingVideo() {
        val mediaStream = factory!!.createLocalMediaStream("ARDAMS")
        mediaStream.addTrack(videoTrackFromCamera)
        mediaStream.addTrack(audioTrackFromLocal)
        peerConnection!!.addStream(mediaStream)
        val message = JSONObject()
        message.put(Constants.CONVERSATION_ID, args.conversationId)
        message.put(Constants.USER_MEDIA, "got user media")
        sendMessage(message)
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
                    message.put("candidate", iceCandidate.sdp)
                    message.put(Constants.USER_MEDIA, "no")
                    message.put(Constants.CONVERSATION_ID, args.conversationId)
                    Log.d(TAG, "onIceCandidate: sending candidate $message")
                    sendMessage(message)
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
                mediaStream.videoTracks.first.addRenderer(VideoRenderer(binding.remoteStream))
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

    private fun createVideoCapturer(): VideoCapturer? {
        val videoCapturer: VideoCapturer?
        videoCapturer = if (useCamera2()) {
            createCameraCapturer(Camera2Enumerator(this))
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
        return Camera2Enumerator.isSupported(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}