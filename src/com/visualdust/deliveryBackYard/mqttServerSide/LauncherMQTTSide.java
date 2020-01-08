package com.visualdust.deliveryBackYard.mqttServerSide;

import com.visualdust.deliveryBackYard.common.EventRW;
import com.visualdust.deliveryBackYard.common.LinedFile;
import com.visualdust.deliveryBackYard.common.Toolbox;
import com.visualdust.deliveryBackYard.delivery.PackageInfo;

import java.io.File;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.UUID;

public class LauncherMQTTSide {
    public static void main(String[] args) {
        Launch();
    }

    public static void Launch() {
        EventRW.Write("---<---BackyardMQTTSide starting up......--->---");

        /**
         * Trying to read config from file. Create one if config file not exist.
         */
        EventRW.Write("@LauncherMQTTSide : Reading configuration......");
        if (!new File("mqttside.config").exists()) {
            EventRW.WriteAsRichText(false, "@LauncherMQTTSide", "mqttside.config file not found.");
            try {
                EventRW.Write("Trying to create mqttside.config file......");
                File configFile = new File("mqttside.config");
                PrintStream printStream = new PrintStream(configFile);
                printStream.print("#mqttside config file\n" +
                        "server-address=tcp://mqtt.visualdust.com\n" +
                        "login-id=+" + "BackYard_" + UUID.randomUUID() + "+\n" +
                        "login-password=\n" +
                        "mqtt-keep-alive-interval=10");
            } catch (Exception e) {
                EventRW.WriteAsRichText(false, "Launcher", "Could not create mqttside.config file. Server will exit.");
                System.exit(1);
            }
        }
        ServerSideMqttClientConfigure configure = new ServerSideMqttClientConfigure(new File("mqttside.config"));
        ServerSideMqttClient mqttClient = new ServerSideMqttClient(configure);

        /**
         * Creating mqtt client
         */
        mqttClient.connect(true);
        (new ClockThread(mqttClient)).start();

        /**
         * For test only
         */
        mqttClient.addResolver(mqttMessageWithTopic -> {
            EventRW.Write("<MQTTMessageReceived>: " + LocalDateTime.now() + " > " + mqttMessageWithTopic.toString());
        });
        mqttClient.addResolver(mqttMessageWithTopic -> {
            if (mqttMessageWithTopic.getTopic().toString().endsWith("test")) {
                String id = new PackageInfo(mqttMessageWithTopic.getMessage().toString()).getID();
                mqttClient.publish("Package received procedure complete", id + "/ServerSideCallback");
            }
        });

        /**
         * Start post processing
         */
        (new PostProcessingThread(mqttClient)).start();
    }


    static class PostProcessingThread extends Thread {

        ServerSideMqttClient mqttClient;

        public PostProcessingThread(ServerSideMqttClient mqttClient) {
            this.mqttClient = mqttClient;
        }

        @Override
        public void run() {
            /**
             * Subscribe pre added topics
             */
            EventRW.Write("@LauncherMQTTSide : Launcher postprocessing......");
            try {
                File topicFile = new File("mqttside.startupsubscribes");
                if (!topicFile.exists()) {
                    EventRW.WriteAsRichText(false, "@LauncherMQTTSide", "mqttside.startupsubscribe not exist");

                    EventRW.Write("Trying to create mqttside.startupsubscribe file......");
                    PrintStream printStream = new PrintStream(topicFile);
                    printStream.print("system/+\nserver/+\ndefault/+\n");

                }
                EventRW.Write("@LauncherMQTTSide : Subscribe pre-added topics from file......");
                sleep(5000);
                LinedFile lf = new LinedFile(topicFile);
                for (int i = 0; i < lf.getLineCount(); i++) {
                    String line = lf.getLineOn(i);
                    if (!line.startsWith("#")) {
                        String[] splitedLf = Toolbox.Split(line, "->", 0);
                        if (splitedLf.length == 1)
                            mqttClient.subscribeTopic(line);
                        else
                            mqttClient.subscribeTopic(splitedLf[0], Integer.valueOf(splitedLf[1]));
                    }
                }
            } catch (Exception e) {
                EventRW.WriteAsRichText(false, "@LauncherMQTTSide", "Could not mqttside.startupsubscribe config file. This will be ignored.");
            }
            //enable terminal?
            terminalMQTTSide = new TerminalMQTTSide(mqttClient);
            //todo cancle the launcher auto start after the main launcher is added
            terminalMQTTSide.start();
        }
    }

    /**
     * Declare the terminal
     */
    public static TerminalMQTTSide terminalMQTTSide = null;

    /**
     * Refresh runtime and status per hour
     */
    static class ClockThread extends Thread {
        ServerSideMqttClient mqttClient;

        public ClockThread(ServerSideMqttClient mqttClient) {
            this.mqttClient = mqttClient;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    EventRW.GainRunTime("mqttside_");
                    EventRW.Write(mqttClient.readStatus(false));
                    sleep(60000 * 60);
                } catch (Exception e) {
                    EventRW.Write(e);
                }
            }
        }
    }
}
