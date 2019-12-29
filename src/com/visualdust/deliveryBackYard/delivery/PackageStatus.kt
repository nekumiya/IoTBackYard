package com.visualdust.deliveryBackYard.delivery

import com.visualdust.deliveryBackYard.commomn.EventRW
import java.lang.Exception
import java.util.*

/**
 * @author VisualDust
 * @since 0.0.0.1
 * last update on 20191229
 */
class PackageStatus {
    var inBoxTempreture: Double = 0.0
    var hasBeenDelivered = false
    var hasBeenSignedFor = false
    var nowLocation: Location? = null
    //todo add all necessary status information here
    var extention = InforExtention()

    constructor() {
    }

}