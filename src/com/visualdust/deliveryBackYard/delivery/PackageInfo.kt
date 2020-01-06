package com.visualdust.deliveryBackYard.delivery

import com.visualdust.deliveryBackYard.infoManagement.InfoExtension
import com.visualdust.deliveryBackYard.commomn.EventRW

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191229
 */
class PackageInfo {
    lateinit protected var id: String
    var name = "null"
    public var extension = InfoExtension()

    /**
     * @param mqttMessage to initialize a [PackageInfo] using a [String]
     * * Formatted message which can be used to initialize a package should be
     * look like this :
     * <split> split <id> split <name> split <tag-key-01> split <tag-value-01> split <tag-key-02> split <tag-value-02>....
     */
    constructor(mqttMessage: String) {
        var messageSplit = mqttMessage[0]
        var items = mqttMessage.split(messageSplit)
        if (items.size < 4 || items.size % 2 != 0) {
            EventRW.WriteAsRichText(false, this.toString(), "Could not use ${mqttMessage} to initialize, format not match. Item[" + items.size + "]=" + items.toString())
        } else {
            id = items[2]
            name = items[3]
            var itemIndex = 4
            while (itemIndex <= items.lastIndex)
                extension.addTag(items[itemIndex++], items[itemIndex++])
        }
    }

    constructor(id: String, name: String) {
        this.name = name
        this.id = id
    }

    public fun getID(): String = this.id
}