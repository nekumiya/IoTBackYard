package com.visualdust.deliveryBackYard;

import com.visualdust.deliveryBackYard.commomn.EventRW;
import com.visualdust.deliveryBackYard.commomn.Resource;
import com.visualdust.deliveryBackYard.delivery.PackageInfo;
import com.visualdust.deliveryBackYard.mqttclient.ServerSideMqttClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

public class Launcher {
    public static void main(String[] args) {
        ServerSideMqttClient mqttClient = new ServerSideMqttClient();
        mqttClient.addResolver(mqttMessageWithTopic -> {
            System.out.println("<MessageReceived>: " + LocalDateTime.now() + " > " + mqttMessageWithTopic.toString());
        });
        mqttClient.addResolver(mqttMessageWithTopic -> {
            String id = new PackageInfo(mqttMessageWithTopic.getMessage().toString()).getID();
            mqttClient.publish("Package received procedure complete", id + "/ServerSideCallback");
        });
        mqttClient.connect(true);
        mqttClient.subscribeTopic("+/test");
        (new ScannerThread(mqttClient)).start();
        (new ClockThread()).start();
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
                    EventRW.Write("Input a message >>>");
                    String message = scanner.nextLine();
                    mqttClient.publish(message, topic);
                } else if (userInput.startsWith("subscribe")) {
                    EventRW.Write("Input a topic would you like to subscribe ");
                    userInput = scanner.nextLine();
                    mqttClient.subscribeTopic(userInput);
                } else if (userInput.startsWith("unsubscribe")) {
                    EventRW.Write("Input a topic would you like to unsubscribe ");
                    userInput = scanner.nextLine();
                    mqttClient.unsubscribeTopic(userInput);
                } else {
                    EventRW.Write("Sorry, you can only publish message or subscribe or unsubscribe topics here.");
                }
            }
        }
    }

    static class ClockThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    EventRW.Write("----------" + LocalDateTime.now() + ">Version=" + Resource.VERSION + ">ServerRuntimeClockBump----------");
                    EventRW.updateTime();
                    sleep(60000 * 60);
                } catch (Exception e) {
                    EventRW.Write(e);
                }
            }
        }
    }
}
