package com.visualdust.deliveryBackYard.mqttServerSide

import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.MqttTopic

class MqttMessageWithTopic {
    public var topic: MqttTopic
    public var message: MqttMessage

    constructor(message: String, topic: String) {
        this.message = MqttMessage(message.toByteArray())
        this.topic = MqttTopic(topic, null)
    }

    constructor(message: MqttMessage?, topic: MqttTopic?) {
        this.topic = topic!!
        this.message = message!!
    }

    override fun toString(): String = "${this.topic}>\"${this.message}\""
}