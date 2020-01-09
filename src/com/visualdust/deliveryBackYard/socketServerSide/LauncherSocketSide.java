package com.visualdust.deliveryBackYard.socketServerSide;

import com.visualdust.deliveryBackYard.common.EventRW;
import com.visualdust.deliveryBackYard.common.Resource;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class LauncherSocketSide {
    public static ServerSocket serverSocket = null;

    public static void main(String[] args) {
        Launch();
    }

    public static void Launch() {
        EventRW.Write("---<---BackyardMQTTSide starting up......--->---");
        /**
         * Trying to read config from file. Create one if config file not exist.
         */
        EventRW.Write("@LauncherSocketSide : Reading configuration......");
        if (!new File(Resource.SOCKETSIDE_CONFIGFILE_NAME).exists()) {
            EventRW.WriteAsRichText(false, Resource.SOCKETSIDE_NAME, Resource.MQTTSIDE_CONFIGFILE_NAME + " not found.");
            try {
                EventRW.Write("Trying to create " + Resource.SOCKETSIDE_CONFIGFILE_NAME + "......");
                File configFile = new File(Resource.SOCKETSIDE_CONFIGFILE_NAME);
                PrintStream printStream = new PrintStream(configFile);
                printStream.print("#" + Resource.SOCKETSIDE_NAME + " config file\n" +
                        "socket-port=22519");
            } catch (Exception e) {
                EventRW.WriteAsRichText(false, Resource.SOCKETSIDE_NAME + "Launcher", "Exception occurred when creating " + Resource.SOCKETSIDE_CONFIGFILE_NAME + " : " + e.toString() + ". Server-socket-side will not be created.");
                return;
            }
        }
        ServerSideSocketConfigure configure = new ServerSideSocketConfigure(new File(Resource.SOCKETSIDE_CONFIGFILE_NAME));

        /**
         * Initialize a socket server using the config file
         */
        HashMap<String, String> configMap = configure.getConfigHashMap();
        int socketPort = 22519;
        if (configMap.containsKey("socket-port"))
            try {
                socketPort = Integer.valueOf(configMap.get("socket-port"));
            } catch (Exception e) {
                EventRW.WriteAsRichText(false, Resource.SOCKETSIDE_NAME + "Launcher", "An exception occurred when analyze the config file : " + e.toString());
            }
        try {
            serverSocket = new ServerSocket(socketPort);
        } catch (Exception e) {
            EventRW.WriteAsRichText(false, Resource.SOCKETSIDE_NAME + "Launcher", "Exception occurred when creating server socket on port : " + socketPort + ". Socket constructor threw " + e.toString() + ". The socket side will not be created.");
            return;
        }

        /**
         * Start the server socket overseeing thread and the terminal
         */
        new SocketServerThread(serverSocket).start();
    }

    /**
     * Declare the terminal
     */
    public static TerminalSocketSide terminalSocketSide = new TerminalSocketSide();

    static class SocketServerThread extends Thread {
        ServerSocket serverSocket;

        SocketServerThread(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            EventRW.WriteAsRichText(true, this.toString(), "Socket on " + serverSocket.getLocalPort() + "Started to oversee.");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    EventRW.Write(Resource.SOCKETSIDE_NAME + " : Connection accepted between " + serverSocket + " and " + socket.getRemoteSocketAddress());
                    SocketAttendant socketAttendant = new SocketAttendant(socket);
                    socketAttendant.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
