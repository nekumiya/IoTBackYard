package com.visualdust.deliveryBackYard.mqttServerSide

import com.visualdust.deliveryBackYard.common.Toolbox

fun main() {
    for (item in Toolbox.Split("help     ", " ", 0)){
        System.out.print(item+",")
    }
}