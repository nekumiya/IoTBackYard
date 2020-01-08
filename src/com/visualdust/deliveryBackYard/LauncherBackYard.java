package com.visualdust.deliveryBackYard;

import com.visualdust.deliveryBackYard.common.EventRW;
import com.visualdust.deliveryBackYard.common.Resource;
import com.visualdust.deliveryBackYard.delivery.TerminalPoolSide;
import com.visualdust.deliveryBackYard.mqttServerSide.LauncherMQTTSide;
import com.visualdust.deliveryBackYard.mqttServerSide.ServerSideMqttClient;
import com.visualdust.deliveryBackYard.mqttServerSide.TerminalMQTTSide;
import com.visualdust.deliveryBackYard.socketServerSide.LauncherSocketSide;
import com.visualdust.deliveryBackYard.socketServerSide.TerminalSocketSide;

import java.util.Scanner;

public class LauncherBackYard {
    static TerminalSocketSide socketSideTerminal = LauncherSocketSide.terminalSocketSide;
    static TerminalMQTTSide mqttSideTerminal;
    static TerminalPoolSide poolSideTerminal = new TerminalPoolSide();

    public static void main(String[] args) {
        /**
         * Launching MqttSide
         */
        LauncherMQTTSide.Launch();
        /**
         * Launching SocketSide
         */
        LauncherSocketSide.Launch();

        new PostProcessingThread().start();
    }

    static class ScannerThread extends Thread {
        Scanner scanner = new Scanner(System.in);

        @Override
        public void run() {
            try {
                sleep(6000);
            } catch (Exception e) {
                EventRW.Write(e);
            }
            while (true) {
                String userInput = scanner.nextLine();
                if (userInput.startsWith("help")) {
                    mqttSideTerminal.run("mqtt-help");
                    socketSideTerminal.run("socket-help");
                    poolSideTerminal.run("pool-help");
                } else if (userInput.startsWith("mqtt")) {
                    mqttSideTerminal.run(userInput);
                } else if (userInput.startsWith("socket")) {
                    socketSideTerminal.run(userInput);
                } else if (userInput.startsWith("pool")) {
                    poolSideTerminal.run(userInput);
                } else {
                    System.out.print("Command not found. Why not ask for \"help\" ?\n" + Resource.COMMAND_PROMPT);
                }
            }
        }
    }

    static class PostProcessingThread extends Thread {
        @Override
        public void run() {
            try {
                sleep(6000);
            } catch (Exception e) {
                EventRW.Write(e);
            }
            mqttSideTerminal = LauncherMQTTSide.terminalMQTTSide;
            (new ClockThread(mqttSideTerminal.getMqttClient())).start();
            new ScannerThread().start();
        }
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
                    EventRW.GainRunTime(Resource.MQTTSIDE_NAME + "_");
                    EventRW.Write(mqttClient.readStatus(false));
                    sleep(60000 * 60);
                } catch (Exception e) {
                    EventRW.Write(e);
                }
            }
        }
    }
}
