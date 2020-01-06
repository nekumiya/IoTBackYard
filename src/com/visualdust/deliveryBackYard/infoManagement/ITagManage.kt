package com.visualdust.deliveryBackYard.infoManagement

import java.lang.Exception

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191229
 */
interface ITagManage {
    /**
     * <p>To add a key-value mapping relationship. In another word, add a tag.</p>
     */
    fun addTag(tag: Tag): String?

    /**
     * <p>find and remove a tag using a key, also delete it's value
     * </p>
     * @param tagKey the key of the tag you wanna remove
     * @throws Exception when key not found
     */
    fun removeTag(tagKey: String)

    /**
     * <p>To set a value of a key. In another word, add a tag
     * </p>
     */
    fun setValueOfKey(key: String, value: String)

    /**
     * <p>To check if there is a key with the name of tag
     * @param key</p>
     */
    fun checkIfThereIs(key: String): Boolean

    /**
     * <p>To get the value of a key in tags
     * </p>
     */
    fun getValueOf(key: String): String?
}