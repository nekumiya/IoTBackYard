package com.visualdust.deliveryBackYard.mqttServerSide

import com.visualdust.deliveryBackYard.common.EventRW
import com.visualdust.deliveryBackYard.common.LinedFile
import com.visualdust.deliveryBackYard.common.Resource
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.MqttTopic
import org.springframework.stereotype.Component
import java.io.File
import java.lang.Exception
import java.util.function.Consumer
import kotlin.collections.HashMap

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191227
 */

@Component
class ServerSideMqttClient {

    public var `server-address` = ""
    var `login-id` = ""

    //Init a mqtt client to oversee messages
    lateinit var mqttClient: MqttClient

    //An publisher
    var publisher = Publisher(this)

    //Can be changed after constructed
    private var mqttConnectOptions = MqttConnectOptions()
        set(value) {
            field = value
        }

    //password is readonly out of secure consideration
    protected var `login-password`: String? = null
        set(value) {
            field = value
        }

    var callBackResolver = ObjectiveMqttCallBack()

    var `mqtt-keep-alive-interval` = 20

    /**
     * <p>Initialize a server-side mqtt client using default configuration</p>
     */
    /**
     * server-address=tcp://mqtt.visualdust.com
     * login-id=server
     * login-password=
     * mqtt-keep-alive-interval=20
     */
    public constructor(configure: ServerSideMqttClientConfigure) {
        try {
            var configMap = configure.configHashMap
            if (configMap.containsKey("server-address"))
                `server-address` = configMap.getValue("server-address")
            if (configMap.containsKey("login-id"))
                `login-id` = configMap.getValue("login-id")
            if (configMap.containsKey("login-password"))
                `login-password` = configMap.getValue("login-password")
            if (configMap.containsKey("mqtt-keep-alive-interval"))
                `mqtt-keep-alive-interval` = Integer.valueOf(configMap.getValue("mqtt-keep-alive-interval"))
        } catch (e: Exception) {
            EventRW.WriteAsRichText(false, this.toString(), "Exception occurred when analyze configuration : $e")
        }
        initialize()
    }

    /**
     * <p>Initialize a server-side mqtt client</p>
     * @param serverAddr declares the address of the mqtt broker with treaty-prefix. for example: "tcp://mqtt.visualdust.com".
     * @param serverIdAsClient declares the id of this server-side client which will be used when connecting to the mqtt broker.
     */
    public constructor(serverAddr: String, serverIdAsClient: String) {
        mqttClient = MqttClient(serverAddr, serverIdAsClient)
        initialize()
    }

    fun initialize() {
        mqttClient = MqttClient(`server-address`, `login-id`)
        mqttConnectOptions.userName = `login-id`
        mqttConnectOptions.password = `login-password`?.toCharArray() ?: charArrayOf()
        mqttConnectOptions.keepAliveInterval = `mqtt-keep-alive-interval`
        mqttConnectOptions.isCleanSession = true
        mqttClient.setCallback(callBackResolver)
    }

    /**
     * <p>This will let the ServerSideMqttClient connect to the broker as a client and subscribe all the pre-subscribed topics. </p>
     * @param autoReconnect announces let the insider mqtt client auto reconnect or not.
     */
    public fun connect(autoReconnect: Boolean = false) {
        mqttConnectOptions.isAutomaticReconnect = autoReconnect
        this.connect(mqttConnectOptions)
    }

    public fun connect(options: MqttConnectOptions) {
        try {
            EventRW.Write("${Resource.MQTTSIDE_NAME}->Client : Trying to connect to : $`server-address`......\n")
            mqttClient.connect(mqttConnectOptions)
            EventRW.WriteAsRichText(true,
                    this.toString(),
                    "Connected to ${mqttClient.currentServerURI} with the identified uid ${mqttConnectOptions.userName}")
        } catch (e: Exception) {
            EventRW.WriteAsRichText(false,
                    this.toString(),
                    "Failed when connecting to ${mqttClient.currentServerURI} with the identified uid ${mqttConnectOptions.userName}, client threw $e")
        }
    }

    public fun disconnect() {
        try {
            EventRW.Write("${Resource.MQTTSIDE_NAME}->Client : Trying to disconnect from : ${mqttClient.currentServerURI}......")
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

    public fun reconnect() {
        try {
            EventRW.Write("${Resource.MQTTSIDE_NAME}->Client : Trying to reconnect to : ${mqttClient.currentServerURI}......")
            mqttClient.reconnect()
            EventRW.WriteAsRichText(true,
                    this.toString(),
                    "Reconnected to ${mqttClient.currentServerURI}")
        } catch (e: Exception) {
            EventRW.WriteAsRichText(false,
                    this.toString(),
                    "Failed when reconnect to ${mqttClient.currentServerURI}, client threw $e")
        }
    }

    public fun readStatus(simplify: Boolean): String {
        var status: String = EventRW.getRuntimeLog("mqttside_") + "\n" +
                "   ---<---Status of $this--->---\n" +
                "   [Client]\n" +
                "       <ClientID>              ${mqttClient.clientId}\n" +
                "       <ResolverCount>         ${callBackResolver.onReceivingResolvers.size}\n" +
                "   [ConnectionStatus]\n" +
                "       <TargetServerAddress>   $`server-address`\n" +
                "       <CurrentServerAddress>  ${mqttClient.currentServerURI}\n" +
                "       <IsConnected>           ${mqttClient.isConnected}\n"
        return status
    }

    //topic subscribing methods

    public fun subscribeTopic(topic: String) {
        try {
            mqttClient.subscribe(topic, 1)
            EventRW.Write("${Resource.MQTTSIDE_NAME}->Client : Subscribed topic \"$topic\" by $this")
        } catch (e: Exception) {
            EventRW.WriteAsRichText(false, this.toString(), "Failed when subscribe topic\" $topic\". Client threw $e")
        }
    }

    public fun subscribeTopic(topic: String, qos: Int) {
        try {
            mqttClient.subscribe(topic, qos)
            EventRW.Write("${Resource.MQTTSIDE_NAME}->Client : Subscribed topic \"$topic\" by $this, qos=$qos")
        } catch (e: Exception) {
            EventRW.WriteAsRichText(false, this.toString(), "Failed when subscribe topic\" $topic\". Client threw $e")
        }
    }

    public fun unsubscribeTopic(topic: String) {
        try {
            mqttClient.unsubscribe(topic)
            EventRW.Write("${Resource.MQTTSIDE_NAME}->Client : Unsubscribed topic \"$topic\" by $this")
        } catch (e: Exception) {
            EventRW.WriteAsRichText(false, this.toString(), "Failed when unsubscribe topic\" $topic\". Client threw $e")
        }
    }

    //resolvers for message arrival
    public fun addResolver(consumer: Consumer<MqttMessageWithTopic>) {
        callBackResolver.addOnReceivingResolver(consumer)
        EventRW.Write("${Resource.MQTTSIDE_NAME}->Client : Added resolver: $consumer to $this")
    }

    public fun publish(message: String, topic: String) = publish(MqttMessageWithTopic(MqttMessage(message.toByteArray()), MqttTopic(topic, null)))

    public fun publish(mqttMessageWithTopic: MqttMessageWithTopic) {
        try {
            publisher.publish(mqttMessageWithTopic)
            EventRW.Write("${Resource.MQTTSIDE_NAME}->Client : $this publish procedure for \"$mqttMessageWithTopic\" finished.")
        } catch (e: Exception) {
            EventRW.WriteAsRichText(false, this.toString(), "Failed when publish message \"$mqttMessageWithTopic\". Client threw $e")
        }
    }
}

class ServerSideMqttClientConfigure {
    var configHashMap: HashMap<String, String>

    constructor(configFile: File) {
        configHashMap = HashMap()
        var lf = LinedFile(configFile)
        for (i in 0 until lf.lineCount - 1) {
            var item = lf.getLineOn(i.toInt())
            if (item.startsWith("#"))
                continue
            if (item.contains("=") && item.split("=").size == 2) {
                var cfg = item.split("=")
                configHashMap.put(cfg[0], cfg[1])
            } else {
                EventRW.WriteAsRichText(false, this.toString(), "Exception occurred when initializing config file at line[$i]<$item> : format error")
            }
        }
    }

    constructor(config: HashMap<String, String>) {
        this.configHashMap = config
    }

}