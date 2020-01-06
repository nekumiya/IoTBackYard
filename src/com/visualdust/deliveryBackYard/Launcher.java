package com.visualdust.deliveryBackYard;

import com.visualdust.deliveryBackYard.commomn.EventRW;
import com.visualdust.deliveryBackYard.commomn.Resource;
import com.visualdust.deliveryBackYard.delivery.PackageInfo;
import com.visualdust.deliveryBackYard.mqttclient.ServerSideMqttClient;
import com.visualdust.deliveryBackYard.mqttclient.ServerSideMqttClientConfigure;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

public class Launcher {
    static LocalDateTime launchTimeStamp = LocalDateTime.now();

    public static void main(String[] args) {
        EventRW.Write("Backyard starting up......");
        EventRW.Write("Reading configuration......");
        if (!new File("config").exists()) {
            EventRW.WriteAsRichText(false, "Launcher", "config file not found. Server will exit");
            System.exit(1);
        }
        ServerSideMqttClientConfigure configure = new ServerSideMqttClientConfigure(new File("config"));
        ServerSideMqttClient mqttClient = new ServerSideMqttClient(configure);

        mqttClient.connect(true);

        (new ScannerThread(mqttClient)).start();
        (new ClockThread(mqttClient)).start();

        //For test only
        mqttClient.addResolver(mqttMessageWithTopic -> {
            System.out.println("<MessageReceived>: " + LocalDateTime.now() + " > " + mqttMessageWithTopic.toString());
        });
        mqttClient.addResolver(mqttMessageWithTopic -> {
            String id = new PackageInfo(mqttMessageWithTopic.getMessage().toString()).getID();
            mqttClient.publish("Package received procedure complete", id + "/ServerSideCallback");
        });
        mqttClient.subscribeTopic("+/test");
    }

    static class ScannerThread extends Thread {
        ServerSideMqttClient mqttClient;
        Scanner scanner = new Scanner(System.in);

        public ScannerThread(ServerSideMqttClient mqttClient) {
            this.mqttClient = mqttClient;
        }

        @Override
        public void run() {
            while (true) {
                String userInput = scanner.nextLine();
                if (userInput.startsWith("publish")) {
                    EventRW.Write("Input a topic where you want to publish your message ");
                    String topic = scanner.nextLine();
                    EventRW.Write("Input a message");
                    String message = scanner.nextLine();
                    mqttClient.publish(message, topic);
                } else if (userInput.startsWith("subscribe")) {
                    EventRW.Write("Input a topic you'd like to subscribe ");
                    userInput = scanner.nextLine();
                    mqttClient.subscribeTopic(userInput);
                } else if (userInput.startsWith("unsubscribe")) {
                    EventRW.Write("Input a topic you'd like to unsubscribe ");
                    userInput = scanner.nextLine();
                    mqttClient.unsubscribeTopic(userInput);
                } else if (userInput.startsWith("connect")) {
                    mqttClient.connect(true);
                } else if (userInput.startsWith("disconnect")) {
                    mqttClient.disconnect();
                } else if (userInput.startsWith("reconnect")) {
                    mqttClient.reconnect();
                } else if (userInput.startsWith("mqtt-status")) {
                    EventRW.Write(mqttClient.readStatus(false));
                } else {
                    EventRW.Write("Unknown command. See what you'd like to do here:\n" +
                            "   [connect]      : manual connect to the broker\n" +
                            "   [disconnect]   : manual disconnect from the broker\n" +
                            "   [subscribe]    : subscribe a topic\n" +
                            "   [unsubscribe]  : unsubscribe a topic\n" +
                            "   [publish]      : publish a message\n" +
                            "   [mqtt-status]       : check the status of mqtt client");
                }
            }
        }
    }

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
