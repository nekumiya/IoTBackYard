package com.visualdust.deliveryBackYard.mqttclient

import com.visualdust.deliveryBackYard.commomn.EventRW
import java.util.*

class Terminal {

    lateinit var mqttClient: ServerSideMqttClient

    constructor(mqttClient: ServerSideMqttClient) {
        this.mqttClient = mqttClient
        ScannerThread(this.mqttClient).start()
    }

    internal class ScannerThread(var mqttClient: ServerSideMqttClient) : Thread() {
        var scanner = Scanner(System.`in`)
        override fun run() {
            while (true) {
                var userInput = scanner.nextLine()
                if (userInput.startsWith("publish")) {
                    EventRW.Write("Input a topic where you want to publish your message ")
                    val topic = scanner.nextLine()
                    EventRW.Write("Input a message")
                    val message = scanner.nextLine()
                    mqttClient.publish(message, topic)
                } else if (userInput.startsWith("subscribe")) {
                    EventRW.Write("Input a topic you'd like to subscribe ")
                    userInput = scanner.nextLine()
                    mqttClient.subscribeTopic(userInput)
                } else if (userInput.startsWith("unsubscribe")) {
                    EventRW.Write("Input a topic you'd like to unsubscribe ")
                    userInput = scanner.nextLine()
                    mqttClient.unsubscribeTopic(userInput)
                } else if (userInput.startsWith("connect")) {
                    mqttClient.connect(true)
                } else if (userInput.startsWith("disconnect")) {
                    mqttClient.disconnect()
                } else if (userInput.startsWith("reconnect")) {
                    mqttClient.reconnect()
                } else if (userInput.startsWith("mqtt-status")) {
                    EventRW.Write(mqttClient.readStatus(false))
                } else {
                    EventRW.Write("Unknown command. See what you'd like to do here:\n" +
                            "   [connect]      : manual connect to the broker\n" +
                            "   [disconnect]   : manual disconnect from the broker\n" +
                            "   [subscribe]    : subscribe a topic\n" +
                            "   [unsubscribe]  : unsubscribe a topic\n" +
                            "   [publish]      : publish a message\n" +
                            "   [mqtt-status]       : check the status of mqtt client")
                }
            }
        }

    }
}