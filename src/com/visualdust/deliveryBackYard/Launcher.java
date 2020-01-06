package com.visualdust.deliveryBackYard;

import com.visualdust.deliveryBackYard.commomn.EventRW;
import com.visualdust.deliveryBackYard.commomn.Resource;
import com.visualdust.deliveryBackYard.delivery.PackageInfo;
import com.visualdust.deliveryBackYard.mqttclient.ServerSideMqttClient;
import com.visualdust.deliveryBackYard.mqttclient.ServerSideMqttClientConfigure;
import com.visualdust.deliveryBackYard.mqttclient.Terminal;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.io.File;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

public class Launcher {
    static LocalDateTime launchTimeStamp = LocalDateTime.now();

    public static void main(String[] args) {
        EventRW.Write("Backyard starting up......");

        /**
         * Trying to read config from file. Create one if config file not exist.
         */
        EventRW.Write("Reading configuration......");
        if (!new File("config").exists()) {
            EventRW.WriteAsRichText(false, "Launcher", "config file not found.");
            try {
                EventRW.Write("Trying to create config file......");
                File configFile = new File("config");
                PrintStream printStream = new PrintStream(configFile);
                printStream.print("#server config file\n" +
                        "server-address=tcp://mqtt.visualdust.com\n" +
                        "login-id=+" + "BackYard_" + UUID.randomUUID() + "+\n" +
                        "login-password=\n" +
                        "mqtt-keep-alive-interval=10");
            } catch (Exception e) {
                EventRW.WriteAsRichText(false, "Launcher", "Could not create config file. Server will exit.");
                System.exit(1);
            }
        }
        ServerSideMqttClientConfigure configure = new ServerSideMqttClientConfigure(new File("config"));
        ServerSideMqttClient mqttClient = new ServerSideMqttClient(configure);

        /**
         * Creating mqtt client...
         */
        mqttClient.connect(true);
        Terminal terminal = new Terminal(mqttClient);
        (new ClockThread(mqttClient)).start();

        /**
         * For test only...
         */
        mqttClient.addResolver(mqttMessageWithTopic -> {
            System.out.println("<MessageReceived>: " + LocalDateTime.now() + " > " + mqttMessageWithTopic.toString());
        });
        mqttClient.addResolver(mqttMessageWithTopic -> {
            if (mqttMessageWithTopic.getTopic().toString().endsWith("test")) {
                String id = new PackageInfo(mqttMessageWithTopic.getMessage().toString()).getID();
                mqttClient.publish("Package received procedure complete", id + "/ServerSideCallback");
            }
        });
        mqttClient.subscribeTopic("+/test");
    }


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
                    EventRW.updateTime();
                    EventRW.Write(mqttClient.readStatus(false));
                    sleep(60000 * 60);
                } catch (Exception e) {
                    EventRW.Write(e);
                }
            }
        }
    }
}
