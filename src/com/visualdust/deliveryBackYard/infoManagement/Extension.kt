package com.visualdust.deliveryBackYard.infoManagement

import com.visualdust.deliveryBackYard.common.EventRW
import java.lang.Exception

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20200105
 */
class Extension<K, T> : ITagManage<K, T> {
    var map: MutableMap<K, T> = HashMap<K, T>()

    /**
     *<p>Will be transformed into addTag(key: K, value: T):Unit
     *</p>
     * @see addTag
     */
    public override fun addTag(tag: Tag<K, T>): T? = addTag(tag.key, tag.value)

    public fun addTag(key: K, value: T) = map.put(key, value)

    public override fun removeTag(tagKey: K) {
        if (map.remove(tagKey) == null)
            EventRW.Write(Exception("$this : exception occurred when removing $tagKey, cause key not found"))
    }

    /**
     * <p> will be transform into setValueOfKey(key: String, value: String):Unit
     * </p>
     * @see setValueOfKey
     */
    public fun setValueOf(tag: Tag<K, T>) = setValueOfKey(tag.key, tag.value)

    public override fun setValueOfKey(key: K, value: T) {
        if (map.get(key) == null)
            map.put(key, value)
        else
            map.set(key, value)
    }

    public override fun checkIfThereIs(key: K): Boolean = map.get(key) != null

    public override fun getValueOf(key: K): T? {
        if (checkIfThereIs(key))
            return map.getValue(key)
        else
            return null
    }
}