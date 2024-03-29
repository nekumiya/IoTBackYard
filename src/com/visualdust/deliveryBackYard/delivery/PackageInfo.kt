package com.visualdust.deliveryBackYard.delivery

import com.visualdust.deliveryBackYard.infoManagement.Extension
import com.visualdust.deliveryBackYard.common.EventRW
import com.visualdust.deliveryBackYard.common.Toolbox

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191229
 */
class PackageInfo {
    lateinit protected var id: String
    var name = "null"
    public var extension = Extension<String, String>()

    /**
     * @param mqttMessage to initialize a [PackageInfo] using a [String]
     * * Formatted message which can be used to initialize a package should be
     * look like this :
     * <split> split <id> split <name> split <tag-key-01> split <tag-value-01> split <tag-key-02> split <tag-value-02>....
     */
    constructor(mqttMessage: String) {
        var items = Toolbox.Split(mqttMessage, mqttMessage[0].toString(), 0)
        if (items.size < 2 || items.size % 2 != 0) {
//            EventRW.WriteAsRichText(false, this.toString(), "Could not use ${mqttMessage} to initialize, format not match. Item[" + items.size + "]=" + items.toString())
            this.id = "null"
        } else {
            id = items[0]
            name = items[1]
            var itemIndex = 2
            while (itemIndex <= items.lastIndex)
                extension.addTag(items[itemIndex++], items[itemIndex++])
        }
    }

    constructor(id: String, name: String) {
        this.name = name
        this.id = id
    }

    public fun getID(): String = this.id

    override fun toString(): String {
        return id + name + super.toString()
    }
}