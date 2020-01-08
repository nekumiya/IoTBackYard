package com.visualdust.deliveryBackYard.socketServerSide

import java.net.http.WebSocket

class SocketPool {
    companion object {
        private var socketDictionary = HashMap<String, WebSocket>()
    }
}