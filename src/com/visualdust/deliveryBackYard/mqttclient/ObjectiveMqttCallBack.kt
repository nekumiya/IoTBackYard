package com.visualdust.deliveryBackYard.mqttclient

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.*
import java.util.function.Consumer
import java.util.function.Function

class ObjectiveMqttCallBack : MqttCallback {
    private var callBackResolvers = Vector<Consumer<PropertiedCallBack>>()

    constructor() {}

    public fun addResolver(consumer: Consumer<PropertiedCallBack>) = callBackResolvers.add(consumer)

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        for (resolver in callBackResolvers)
            resolver.accept(PropertiedCallBack(topic, message.toString()))
    }

    override fun connectionLost(p0: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deliveryComplete(p0: IMqttDeliveryToken?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}