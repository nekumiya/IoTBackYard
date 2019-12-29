package com.visualdust.deliveryBackYard.mqttclient

import com.visualdust.deliveryBackYard.commomn.EventRW
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.*
import java.util.function.Consumer

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191229
 */
class ObjectiveMqttCallBack : MqttCallback {
    private var onReceivingResolvers = Vector<Consumer<PropertiedCallBack>>()

    constructor() {}

    /**
     *@param consumer is a consumer function allows only one argument(received message) pass into it and return nothing
     * @see Consumer
     * @see PropertiedCallBack
     */
    public fun addOnReceivingResolver(consumer: Consumer<PropertiedCallBack>) = onReceivingResolvers.add(consumer)

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        for (resolver in onReceivingResolvers)
            resolver.accept(PropertiedCallBack(topic, message.toString()))
    }

    override fun connectionLost(p0: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deliveryComplete(deliveryToken: IMqttDeliveryToken?) {
        var token = deliveryToken as MqttDeliveryToken
        EventRW.WriteAsRichText(true,token.toString(),", Delivery complete")
    }
}