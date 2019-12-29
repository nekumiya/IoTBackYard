package com.visualdust.deliveryBackYard.delivery

import java.util.*

class PackageInfo {
    lateinit protected var id: String
    var name = ""
    var description = "A package"
    lateinit var sender: ConsumerInfo
    lateinit var receiver: ConsumerInfo
    lateinit var status: PackageStatus
    //TODO add all package properties here

    constructor() {}
    constructor(id: String, name: String) {
        this.name = name
        this.id = id
    }
}