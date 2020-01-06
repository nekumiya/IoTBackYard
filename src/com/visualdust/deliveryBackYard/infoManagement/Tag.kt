package com.visualdust.deliveryBackYard.infoManagement

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191229
 */
class Tag {
    lateinit var key: String
    lateinit var value: String

    /**
     * <p>"tag" class type is here to provide some kind of instant key-value mapping
     *     function but can also be stored independently. Inside of it are two variables called
     *     "key" and "value". You can treat it simply as a tag or establish a mapping relationship
     *     using it.</p>
     *
     * @param key key
     * @param value inside-contain
     */
    constructor(key: String, value: String) {
        this.key = key
        this.value = value
    }
}