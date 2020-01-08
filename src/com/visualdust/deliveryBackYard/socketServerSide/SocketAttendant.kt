package com.visualdust.deliveryBackYard.socketServerSide

import java.net.Socket
import java.net.http.WebSocket

class SocketAttendant : Thread {
    var uid: String = "null"
    lateinit var socket: Socket

    constructor() {}
}