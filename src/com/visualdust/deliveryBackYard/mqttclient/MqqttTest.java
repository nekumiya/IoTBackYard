package com.visualdust.deliveryBackYard.mqttclient;

import com.visualdust.deliveryBackYard.delivery.PackageInfo;

import java.util.function.Consumer;

public class MqqttTest {
    public static void main(String[] args) {
        ServerSideMqttClient mqttClient = new ServerSideMqttClient();
        mqttClient.addResolver(propertiedCallBack -> System.out.println(propertiedCallBack.toString()));
        mqttClient.connect(false);
        mqttClient.subscribeTopic("test");
        mqttClient.publish("lalala","test");
    }
}
