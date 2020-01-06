package com.visualdust.deliveryBackYard.mqttclient

import com.visualdust.deliveryBackYard.commomn.Toolbox

fun main() {
    for (item in Toolbox.Split("help     ", " ", 0)){
        System.out.print(item+",")
    }
}