package com.visualdust.deliveryBackYard.infoManagement

import java.lang.Exception

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191229
 */
interface ITagManage<K, T> {
    /**
     * <p>To add a key-value mapping relationship. In another word, add a tag.</p>
     */
    fun addTag(tag: Tag<K, T>): T?

    /**
     * <p>find and remove a tag using a key, also delete it's value
     * </p>
     * @param tagKey the key of the tag you wanna remove
     * @throws Exception when key not found
     */
    fun removeTag(tagKey: K)

    /**
     * <p>To set a value of a key. In another word, add a tag
     * </p>
     */
    fun setValueOfKey(key: K, value: T)

    /**
     * <p>To check if there is a key with the name of tag
     * @param key</p>
     */
    fun checkIfThereIs(key: K): Boolean

    /**
     * <p>To get the value of a key in tags
     * </p>
     */
    fun getValueOf(key: K): T?
}