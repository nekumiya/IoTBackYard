package com.visualdust.deliveryBackYard.socketServerSide;

import com.visualdust.deliveryBackYard.common.EventRW;
import com.visualdust.deliveryBackYard.common.Resource;

import java.io.File;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.UUID;

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
//                printStream.print("#mqttside config file\n" +
//                        "server-address=tcp://mqtt.visualdust.com\n" +
//                        "login-id=+" + "BackYard_" + UUID.randomUUID() + "+\n" +
//                        "login-password=\n" +
//                        "mqtt-keep-alive-interval=10");
            } catch (Exception e) {
                EventRW.WriteAsRichText(false, Resource.SOCKETSIDE_NAME + "Launcher", "Could not create " + Resource.SOCKETSIDE_CONFIGFILE_NAME + ". Server will exit.");
                System.exit(1);
            }
            /**
             * Initialize a socket server using the config file
             */
        }
    }
}
