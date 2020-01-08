package com.visualdust.deliveryBackYard.infoManagement

import com.visualdust.deliveryBackYard.common.EventRW
import java.lang.Exception

class UnrestrainedExtension : ITagManage<Any, Any> {
    companion object {}

    var map: MutableMap<Any, Any> = HashMap<Any, Any>()
    /**
     * In order to search value of a specific class type
     */
    var typeDictionary = HashMap<Class<Any>, MutableList<Any>>()

    /**
     *<p>Will be transformed into addTag(key: Any, value: Any):Unit
     *</p>
     * @see addTag
     */
    public override fun addTag(tag: Tag<Any, Any>): Any? = addTag(tag.key, tag.value)

    public fun addTag(key: Any, value: Any) :Any?{
        return map.put(key, value)
    }

    public override fun removeTag(tagKey: Any) {
        if (map.remove(tagKey) == null)
            EventRW.Write(Exception("$this : exception occurred when removing $tagKey, cause key not found"))
    }

    /**
     * <p> will be transform into setValueOfKey(key: String, value: String):Unit
     * </p>
     * @see setValueOfKey
     */
    public fun setValueOf(tag: Tag<Any, Any>) = setValueOfKey(tag.key, tag.value)

    public override fun setValueOfKey(key: Any, value: Any) {
        if (map.get(key) == null)
            map.put(key, value)
        else
            map.set(key, value)
    }

    public override fun checkIfThereIs(key: Any): Boolean = map.get(key) != null

    public override fun getValueOf(key: Any): Any? {
        if (checkIfThereIs(key))
            return map.getValue(key)
        else
            return null
    }

}