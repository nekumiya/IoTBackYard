package com.visualdust.deliveryBackYard.mqttclient

import com.visualdust.deliveryBackYard.commomn.EventRW
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.function.Consumer

/**
 * @author VisualDust
 * 20191227
 */

@Component
class ServerSideMqttClient {

    companion object {
        @JvmField
        var serverAddr = "tcp://mqtt.visualdust.com"
        @JvmField
        var serverIdAsClient = "server"

    }

    private var initialSubscribedTopics: List<String> = listOf(
            //Here you can put all the initial-subscribe-needed topics here
            "admin", "control", "global", "system"
    )

    //Init a mqtt client to oversee messages
    var mqttClient = MqttClient(serverAddr, serverIdAsClient)

    //An in-queue publisher
    var publisher = Publisher(this)

    //Can be changed after constructed
    private var mqttConnectOptions = MqttConnectOptions()
        set(value) {
            field = value
        }

    protected var password: String? = null
        set(value) {
            field = value
        }

    var callBackResolver = ObjectiveMqttCallBack()

    public constructor() {

    }

    init {
        //Initialize user name and password
        mqttConnectOptions.userName = serverIdAsClient
        mqttConnectOptions.password = password?.toCharArray() ?: charArrayOf()
        //Set bump interval
        mqttConnectOptions.keepAliveInterval = 10
        mqttClient.setCallback(callBackResolver)
    }

    public fun connect(autoReconnect: Boolean = false) {
        mqttConnectOptions.isAutomaticReconnect = autoReconnect
        try {
            mqttClient.connect(mqttConnectOptions)
            EventRW.WriteAsRichText(true,
                    this.toString(),
                    "Connected to ${mqttClient.currentServerURI} with the identified uid${mqttConnectOptions.userName}")
            //Subscribe topics
            for (topic in initialSubscribedTopics)
                mqttClient.subscribe(topic)
        } catch (e: Exception) {
            EventRW.WriteAsRichText(false,
                    this.toString(),
                    "Failed when connecting to ${mqttClient.currentServerURI} with the identified uid${mqttConnectOptions.userName}, client threw $e")
        }
    }

    public fun disconnect() {
        try {
            mqttClient.disconnect()
            EventRW.WriteAsRichText(true,
                    this.toString(),
                    "Disconnected from ${mqttClient.currentServerURI}")
        } catch (e: Exception) {
            EventRW.WriteAsRichText(false,
                    this.toString(),
                    "Failed when disconnecting from ${mqttClient.currentServerURI}, client threw $e")
        }
    }


    //topic subscribing methods

    public fun subscribeTopic(topic: String) = mqttClient.subscribe(topic)
    public fun unsubscribeTopic(topic: String) = mqttClient.unsubscribe(topic)
    public fun subscribeTopics(topics: Collection<String>) = mqttClient.subscribe(topics.toTypedArray())
    public fun unsubscribeTopics(topics: Collection<String>) = mqttClient.unsubscribe(topics.toTypedArray())

    //resolvers for message arrival
    public fun addResolver(consumer: Consumer<PropertiedCallBack>) = callBackResolver.addResolver(consumer)

}