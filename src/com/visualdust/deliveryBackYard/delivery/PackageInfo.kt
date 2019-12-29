package com.visualdust.deliveryBackYard.delivery

import com.visualdust.deliveryBackYard.commomn.EventRW
import java.lang.Exception
import java.util.*

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191229
 */
class PackageInfo {
    lateinit protected var id: String
    var name = ""
    var description = "A package"
    lateinit var sender: ConsumerInfo
    lateinit var receiver: ConsumerInfo
    lateinit var status: PackageStatus
    //TODO add all package properties here


    constructor(id: String, name: String) {
        this.name = name
        this.id = id
    }


}