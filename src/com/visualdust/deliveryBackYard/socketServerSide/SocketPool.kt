package com.visualdust.deliveryBackYard.socketServerSide

import java.net.http.WebSocket

class SocketPool {
    companion object {
        private var `uid-skt-Dictionary` = HashMap<String, WebSocket>()

        fun drop(){

        }
    }
}