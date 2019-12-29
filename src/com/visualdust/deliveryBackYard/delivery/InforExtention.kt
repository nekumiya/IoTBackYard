package com.visualdust.deliveryBackYard.delivery

import com.visualdust.deliveryBackYard.commomn.EventRW
import java.lang.Exception

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191229
 */
class InforExtention {
    var map: MutableMap<String, String> = mutableMapOf()

    /**
     *<p>Will be transformed into addTag(key: String, value: String):Unit
     *</p>
     * @see addTag
     */
    fun addTag(tag: Tag) = addTag(tag.key, tag.value)

    /**
     * <p>To add a key-value mapping relationship. In another word, add a tag.</p>
     */
    fun addTag(key: String, value: String) = map.put(key, value)

    /**
     * <p>find and remove a tag using a key, also delete it's value
     * </p>
     * @param tagKey the key of the tag you wanna remove
     * @throws Exception when key not found
     */
    fun removeTag(tagKey: String) {
        if (map.remove(tagKey).equals(null))
            EventRW.Write(Exception("$this : exception occurred when removing $tagKey, cause key not found"))
    }

    /**
     * <p> will be transform into setValueOfKey(key: String, value: String):Unit
     * </p>
     * @see setValueOfKey
     */
    fun setValueOfKey(tag: Tag) = setValueOfKey(tag.key, tag.value)

    /**
     * <p>To set a value of a key. In another word, add a tag
     * </p>
     */
    fun setValueOfKey(key: String, value: String) {
        if (map.get(key).equals(null))
            map.put(key, value)
        else
            map.set(key, value)
    }

    internal fun checkIfThereIs(key: String): Boolean = !map.get(key).equals(null)

    /**
     * <p>To get the value of a key in tags
     * </p>
     */
    fun getValueOf(key: String): String? {
        if (checkIfThereIs(key))
            return map.getValue(key)
        else
            return null
    }
}