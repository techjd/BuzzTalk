package com.ssip.buzztalk.utils

object Constants {
    const val DEVELOPMENT_BASE_URL = "http://192.168.64.135:5500/api/"
    const val PRODUCTION_BASE_URL = "" // Enter Deployed Server URL
    const val CHAT_SERVER_URL = "http://192.168.64.135:5500/"
    const val PREFS_TOKEN_FILE = "PREFS_TOKEN_FILE"
    const val USER_TOKEN = "USER_TOKEN"
    const val USER_ID = "USER_ID"
    const val CONVERSATION_ID = "conversationId" // Key for Video Call
    const val USER_MEDIA = "userMedia" // Key When we are sending user media
    const val REGISTER_ENDPOINT = "auth/register"
    const val LOGIN_ENDPOINT = "auth/login"
    const val GET_USER_INFO_ENDPOINT = "user/userInfo"
    const val GET_USER_INFO_OF_OTHER_USERS_ENDPOINT = "user/getOthersInfo"
    const val GET_SEARCH_USERS = "user/getAllUsers"
    const val FOLLOW_USER = "user/follow"
    const val UNFOLLOW_USER  = "user/unfollow"
    const val GET_ALL_FOLLOWERS_AND_FOLLOWING = "user/getAllFollowersAndFollowing"
    const val CHECK_IF_USER_FOLLOWED_OR_NOT = "user/checkIfUserFollowedOrNot"
    const val SEND_REQUEST = "user/connect"
    const val CHECK_IF_REQUEST_SENT_OR_NOT = "user/checkIfRequestSentOrNot"
    const val GET_ALL_CONNECTIONS_REQUESTS = "user/getAllConnectionsRequest"
    const val ACCEPT_REQUEST = "user/acceptRequest"
    const val GET_ALL_CONNECTIONS = "user/getAllConnections"
    const val MAKE_ME_ONLINE = "chat/makeMeOnline"
    const val REMOVE_ME_ONLINE = "chat/removeMeOnline"
    const val SEND_MESSAGE = "chat/sendMessage"
    const val GET_ALL_MESSAGES = "chat/getAllMessages"
    const val GET_ALL_CONVERSATIONS = "chat/getAllConversations"
    const val GET_USER_STATUS = "chat/getUserStatus"

    // Constants Defined in the backend to cross verify in frontend
    const val SUCCESS = "SUCCESS"
    const val FAILURE = "FAILURE"
    const val USER_FOLLOWED = "USER FOLLOWED"

    const val USER_NOT_CONNECTED = "USER NOT CONNECTED"
    const val REQUEST_SENT = "REQUEST SENT"
    const val UNSEND_REQUEST = "REQUEST UNSEND"
    const val REQUEST_ACCEPTED = "REQUEST ACCEPTED"
    const val REQUEST_REJECTED  = "REQUEST REJECTED"
    const val ALL_CONNECTIONS = "ALL CONNECTIONS"
    const val NEW_MESSAGE = "NEW MESSAGE"
    const val USER_OFFLINE = "USER OFFLINE"
    const val USER_ONLINE = "USER ONLINE"
}