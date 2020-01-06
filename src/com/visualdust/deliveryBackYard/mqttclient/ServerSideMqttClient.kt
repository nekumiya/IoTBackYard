package com.visualdust.deliveryBackYard.mqttclient

import com.visualdust.deliveryBackYard.commomn.EventRW
import com.visualdust.deliveryBackYard.commomn.LinedFile
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
            EventRW.Write("Trying to connect to : $`server-address`......")
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
            EventRW.Write("Trying to disconnect from : ${mqttClient.currentServerURI}......")
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
            EventRW.Write("Trying to reconnect to : ${mqttClient.currentServerURI}......")
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
        var status: String = EventRW.getRuntimeLog() + "\n" +
                "---<Status of $this>---\n" +
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
        mqttClient.subscribe(topic, 1)
        EventRW.Write("Subscribed topic \"$topic\" by $this")
    }

    public fun subscribeTopic(topic: String, qos: Int) {
        mqttClient.subscribe(topic, qos)
        EventRW.Write("Subscribed topic \"$topic\" by $this, qos=$qos")
    }

    public fun unsubscribeTopic(topic: String) {
        mqttClient.unsubscribe(topic)
        EventRW.Write("Unsubscribed topic \"$topic\" by $this")
    }

    //resolvers for message arrival
    public fun addResolver(consumer: Consumer<MqttMessageWithTopic>) {
        callBackResolver.addOnReceivingResolver(consumer)
        EventRW.Write("Added resolver: $consumer to $this")
    }

    public fun publish(message: String, topic: String) = publish(MqttMessageWithTopic(MqttMessage(message.toByteArray()), MqttTopic(topic, null)))

    public fun publish(mqttMessageWithTopic: MqttMessageWithTopic) {
        publisher.publish(mqttMessageWithTopic)
        EventRW.Write("$this published \"$mqttMessageWithTopic\"")
    }
}

class ServerSideMqttClientConfigure {
    var configHashMap: HashMap<String, String>

    constructor(configFile: File) {
        configHashMap = HashMap()
        var lf = LinedFile(File("config"))
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