package com.visualdust.deliveryBackYard.mqttclient

class PropertiedCallBack {
    public var topic: String = ""
    public var message: String = ""

    constructor(topic: String?, message: String?) {
        this.topic = topic!!
        this.message = message!!
    }

    override fun toString(): String = "${this.topic}//${this.message}"
}