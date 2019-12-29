package com.visualdust.deliveryBackYard.delivery

import com.visualdust.deliveryBackYard.commomn.EventRW
import java.lang.Exception
import java.util.*

class PackageStatus {
    var inBoxTempreture: Double = 0.0
    var hasBeenDelivered = false
    var hasBeenSignedFor = false
    var nowLocation: Location? = null
    //todo add all necessary status information here
    var map: MutableMap<String, String> = mutableMapOf()

    constructor() {

    }

    fun addTag(tag: Tag) = map.put(tag.key, tag.value)
    /**
     * <p>find and remove a tag using a key, also delete it's value
     * </p>
     * @param tagKey the key of the tag you wanna remove
     * @throws Exception when key not found
     * @author VisualDust
     */
    fun removeTag(tagKey: String) {
        if (map.remove(tagKey).equals(null))
            EventRW.Write(Exception("$this : exception occurred when removing $tagKey, cause key not found"))
    }

    /**
     * <p> will be transform into setValueOfKey(key: String, value: String):Unit
     * </p>
     * @see setValueOfKey
     * @author VisualDust
     */
    fun setValueOfKey(tag: Tag) = setValueOfKey(tag.key, tag.value)

    /**
     * <p>To set a value of a key
     * </p>
     */
    fun setValueOfKey(key: String, value: String) {
        if (map.get(key).equals(null))
            map.put(key, value)
        else
            map.set(key, value)
    }
}