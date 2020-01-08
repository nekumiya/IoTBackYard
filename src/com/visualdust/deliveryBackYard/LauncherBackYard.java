package com.visualdust.deliveryBackYard;

import com.visualdust.deliveryBackYard.mqttServerSide.LauncherMQTTSide;
import com.visualdust.deliveryBackYard.mqttServerSide.TerminalMQTTSide;

public class LauncherBackYard {
    public static void main(String[] args) {
        /**
         * Launching MqttSide
         */
        LauncherMQTTSide.Launch();
        TerminalMQTTSide mqttTerminal = LauncherMQTTSide.terminalMQTTSide;

        /**
         * Launching SocketSide
         */
    }
}
