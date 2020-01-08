package com.visualdust.deliveryBackYard.infoManagement.user

import com.visualdust.deliveryBackYard.infoManagement.Extension
import java.util.*

class User {
    private var id = ""
    var userName = ""
    var extension = Extension<String, String>()

    constructor(id: String?, userName: String?) {
        this.id = id ?: UUID.randomUUID().toString()
        this.userName = userName ?: this.id
    }
}