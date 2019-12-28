package com.visualdust.deliveryBackYard.delivery

import java.util.*

class PackageInfo {
    protected var id = UUID.randomUUID().toString()
    var name = ""
    var description = "A package"
    var inBoxTempreture: Double = 0.0
    var hasBeenDelivered = false
    //TODO add all package properties here

    constructor() {}
    constructor(name: String, id: String) {
        this.name = name
        this.id = id
    }
}