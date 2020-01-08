package com.visualdust.deliveryBackYard.socketServerSide

import java.net.http.WebSocket

class SocketPool {
    companion object {
        @JvmField
        var socketDictionary = HashMap<String, WebSocket>()
    }
}