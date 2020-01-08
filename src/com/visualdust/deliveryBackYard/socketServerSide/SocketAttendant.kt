package com.visualdust.deliveryBackYard.socketServerSide

import com.visualdust.deliveryBackYard.common.EventRW
import com.visualdust.deliveryBackYard.common.Resource
import java.io.*
import java.lang.Exception
import java.net.Socket
import java.util.function.Consumer

class SocketAttendant : Thread {
    lateinit var uid: String
    public lateinit var socket: Socket

    lateinit var bufferedSocketReader: BufferedReader
    lateinit var bufferedSocketWriter: BufferedWriter

    var resolvers: MutableList<Consumer<MessageFromAttendant>> = mutableListOf()

    constructor(socket: Socket) {
        this.socket = socket
        Initialize()
    }

    constructor(uid: String, socket: Socket) {
        this.uid = uid
        this.socket = socket
        Initialize()
    }

    fun Initialize() {
        bufferedSocketReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        bufferedSocketWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
        //todo add necessary builtin resolvers here
    }

    /**
     * @return return itself allows you to add resolvers as a chain
     */
    fun addResolver(resolver: Consumer<MessageFromAttendant>): SocketAttendant {
        this.resolvers.add(resolver)
        return this
    }

    override fun run() {
        try {
            //todo standardize key words via publish a documentation?
            bufferedSocketWriter.write("handshake,${this.toString()},${Resource.VERSION}")
            /**
             * Flushes the stream.  If the stream has saved any characters from the
             * various write() methods in a buffer, write them immediately to their
             * intended destination.  Then, if that destination is another character or
             * byte stream, flush it.  Thus one flush() invocation will flush all the
             * buffers in a chain of Writers and OutputStreams.*/
            bufferedSocketWriter.flush()
        } catch (e: Exception) {
            EventRW.WriteAsRichText(false, this.toString(), "An exception occurred when writing stream to the socket buffer. Writer threw $e")
        }
        while (true) {
            try {
                var message = bufferedSocketReader.readLine()
                for (resolver in resolvers)
                    resolver.accept(MessageFromAttendant(message, this))
            } catch (e: Exception) {
                EventRW.WriteAsRichText(false, this.toString(), "An exception occurred when reading stream from the socket buffer. Reader threw $e")
            }
        }
    }
}

class MessageFromAttendant {
    private var message: String
        get() = message
    private var socketAttendant: SocketAttendant
        get() = socketAttendant

    constructor(message: String, socketAttendant: SocketAttendant) {
        this.message = message
        this.socketAttendant = socketAttendant
    }
}