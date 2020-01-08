package com.visualdust.deliveryBackYard.delivery

import com.visualdust.deliveryBackYard.infoManagement.Extension

/**
 * ConsumerInfo
 * <p>
 *     ConsumerInfo class type contains a temporary and instant status of a consumer.
 *     Only be used to describe the status of a consumer when he send a package
 * </p>
 * @since 0.0.0.1
 * @author VisualDust
 */
class ConsumerInfo {
    lateinit var uid: String
    var name: String = "Consumer"
    var extension = Extension<String, String>()
}