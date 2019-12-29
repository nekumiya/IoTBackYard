package com.visualdust.deliveryBackYard.delivery

class Tag {
    lateinit var key: String
    lateinit var value: String

    /**
     * <p>
     *     "tag class type" is established to provide some kind of instant key-value mapping
     *     function but can also be stored independently
     * </p>
     * @param key key
     * @param value inside-contain
     * @since 0.0.0.1
     * @author VisualDust
     */
    constructor(key: String, value: String) {
        this.key = key
        this.value = value
    }
}