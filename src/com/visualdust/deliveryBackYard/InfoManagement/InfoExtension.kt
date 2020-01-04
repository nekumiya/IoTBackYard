package com.visualdust.deliveryBackYard.InfoManagement

import com.visualdust.deliveryBackYard.commomn.EventRW
import java.lang.Exception

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191229
 */
class InfoExtension : ITagManage {
    var map: MutableMap<String, String> = mutableMapOf()

    /**
     *<p>Will be transformed into addTag(key: String, value: String):Unit
     *</p>
     * @see addTag
     */
    override fun addTag(tag: Tag): String? = addTag(tag.key, tag.value)

    fun addTag(key: String, value: String) = map.put(key, value)

    override fun removeTag(tagKey: String) {
        if (map.remove(tagKey).equals(null))
            EventRW.Write(Exception("$this : exception occurred when removing $tagKey, cause key not found"))
    }

    /**
     * <p> will be transform into setValueOfKey(key: String, value: String):Unit
     * </p>
     * @see setValueOfKey
     */
    fun setValueOf(tag: Tag) = setValueOfKey(tag.key, tag.value)

    override fun setValueOfKey(key: String, value: String) {
        if (map.get(key).equals(null))
            map.put(key, value)
        else
            map.set(key, value)
    }

    override fun checkIfThereIs(key: String): Boolean = !map.get(key).equals(null)

    override fun getValueOf(key: String): String? {
        if (checkIfThereIs(key))
            return map.getValue(key)
        else
            return null
    }
}