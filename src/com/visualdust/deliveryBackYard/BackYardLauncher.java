package com.visualdust.deliveryBackYard;

import com.visualdust.deliveryBackYard.mqttServerSide.LauncherMQTTSide;
import com.visualdust.deliveryBackYard.mqttServerSide.MqttServerSideTerminal;

public class BackYardLauncher {
    public static void main(String[] args) {
        /**
         * Launching MqttSide
         */
        LauncherMQTTSide.Launch();
        MqttServerSideTerminal mqttTerminal = LauncherMQTTSide.mqttServerSideTerminal;

        /**
         * Launching SocketSide
         */
    }
}
