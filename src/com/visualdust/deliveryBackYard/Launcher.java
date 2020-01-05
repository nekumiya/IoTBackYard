package com.visualdust.deliveryBackYard;

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
            mqttClient.publish("Package received procedure complete", id+"/ServerSideCallback");
        });
        mqttClient.connect(true);
        mqttClient.subscribeTopic("+/test");
        (new ScannerThread(mqttClient)).start();
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
                if (userInput.startsWith("subscribe")) {
                    System.out.println("Input a topic would you like to subscribe: ");
                    userInput = scanner.nextLine();
                    mqttClient.subscribeTopic(userInput);
                } else if (userInput.startsWith("unsubscribe")) {
                    System.out.println("Input a topic would you like to unsubscribe: ");
                    userInput = scanner.nextLine();
                    mqttClient.unsubscribeTopic(userInput);
                } else {
                    System.out.println("Sorry, you can only subscribe or unsubscribe topics here.");
                }
            }
        }
    }
}
