package com.visualdust.deliveryBackYard.mqttclient;

import java.util.function.Consumer;

public class MqqttTest {
    public static void main(String[] args) {
        ServerSideMqttClient mqttClient = new ServerSideMqttClient();
        mqttClient.addResolver(new Consumer<PropertiedCallBack>() {
            @Override
            public void accept(PropertiedCallBack propertiedCallBack) {
                System.out.println(propertiedCallBack.toString());
            }
        });
        mqttClient.connect(false);
    }
}
