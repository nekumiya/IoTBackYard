package com.visualdust.deliveryBackYard.mqttServerSide

import com.visualdust.deliveryBackYard.common.EventRW
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
    public var onReceivingResolvers = Vector<Consumer<MqttMessageWithTopic>>()
    public var onConnectionLostResolver: Consumer<Throwable>? = null
        set(value) {
            field = value
        }

    constructor() {}

    /**
     *@param consumer is a consumer function allows only one argument(received message) pass into it and return nothing
     * @see Consumer
     * @see MqttMessageWithTopic
     */
    public fun addOnReceivingResolver(consumer: Consumer<MqttMessageWithTopic>) = onReceivingResolvers.add(consumer)

    override fun messageArrived(topic: String, message: MqttMessage?) {
        for (resolver in onReceivingResolvers)
            resolver.accept(MqttMessageWithTopic(message.toString(), topic))
    }

    override fun connectionLost(p0: Throwable?) {
        if (p0 != null)
            onConnectionLostResolver?.accept(p0)
    }

    override fun deliveryComplete(deliveryToken: IMqttDeliveryToken?) {
        var token = deliveryToken as MqttDeliveryToken
        EventRW.WriteAsRichText(true, token.toString(), ", Delivery complete")
    }
}