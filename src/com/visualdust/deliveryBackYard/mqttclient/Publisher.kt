package com.visualdust.deliveryBackYard.mqttclient

import com.visualdust.deliveryBackYard.commomn.EventRW
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.MqttTopic
import java.lang.Exception
import java.util.*

class Publisher {
    companion object {
        fun getRandomUUID(): String = UUID.randomUUID().toString()
    }

    lateinit var mqttClient: ServerSideMqttClient
    var messageVector = mutableListOf<MqttMessageWithTopic>()
    var publishingThread = PublishingThread(this)
    var publishTimeOut: Long = 10

    constructor(mqttClient: ServerSideMqttClient) {
        this.mqttClient = mqttClient
    }

    fun publishToQueue(message: MqttMessageWithTopic) {
        messageVector.add(message)
        if (!publishingThread.isAlive)
            publishingThread.start()
    }

    class PublishingThread : Thread {
        lateinit var publisher: Publisher;

        constructor(publisher: Publisher) {
            this.publisher = publisher
        }

        override fun run() {
            var mqttClient = publisher.mqttClient.mqttClient
            while (!publisher.messageVector.isEmpty()) {
                var nowMessage = publisher.messageVector.elementAt(0)
                var mqttToken = MqttDeliveryToken()
                try {
                    nowMessage.topic = mqttClient.getTopic(nowMessage.topic.toString())
                    mqttToken = nowMessage.topic.publish(nowMessage.message)
                    mqttToken.waitForCompletion(publisher.publishTimeOut)
                    EventRW.WriteAsRichText(true, this.toString(),
                            "Message ${nowMessage}published successfully")
                } catch (e: Exception) {
                    EventRW.WriteAsRichText(false, this.toString(),
                            "Failed when publishing message $nowMessage")
                }
                publisher.messageVector.removeAt(0)
            }
        }
    }
}

/**
 * <p> packs up a single topic and a piece of message to make them into a single arg.
 * </p>
 */
class MqttMessageWithTopic {
    lateinit var topic: MqttTopic
    lateinit var message: MqttMessage

    constructor(message: MqttMessage, topic: MqttTopic) {
        this.topic = topic
        this.message = message
    }

    override fun toString(): String = "$topic//${message.payload}"

}