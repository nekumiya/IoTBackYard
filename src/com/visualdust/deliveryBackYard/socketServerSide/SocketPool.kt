package com.visualdust.deliveryBackYard.socketServerSide

class SocketPool {
    companion object {
        public var `sid-sktat-Dictionary` = HashMap<String, SocketAttendant>()
        public var `uid-sktat-Dictionary` = HashMap<String, SocketAttendant>()

        fun drop(it: SocketAttendant) {
            `sid-sktat-Dictionary`.put(it.sid, it)
            `uid-sktat-Dictionary`.put(it.uid, it)
        }

        fun findBySid(socketID: String): SocketAttendant? = `sid-sktat-Dictionary`.getValue(socketID)
        fun findByUid(userID: String): SocketAttendant? = `uid-sktat-Dictionary`.getValue(userID)
    }
}