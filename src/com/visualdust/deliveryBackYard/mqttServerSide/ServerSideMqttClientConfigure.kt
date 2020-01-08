package com.visualdust.deliveryBackYard.mqttServerSide

import com.visualdust.deliveryBackYard.common.EventRW
import com.visualdust.deliveryBackYard.common.LinedFile
import java.io.File

class ServerSideMqttClientConfigure {
    var configHashMap: HashMap<String, String> = HashMap()

    constructor(configFile: File) {
        var lf = LinedFile(configFile)
        for (i in 0 until lf.lineCount - 1) {
            var item = lf.getLineOn(i.toInt())
            if (item.startsWith("#"))
                continue
            if (item.contains("=") && item.split("=").size == 2) {
                var cfg = item.split("=")
                configHashMap.put(cfg[0], cfg[1])
            } else {
                EventRW.WriteAsRichText(false, this.toString(), "Exception occurred when initializing config file at line[$i]<$item> : format error")
            }
        }
    }

    constructor(config: HashMap<String, String>) {
        this.configHashMap = config
    }
}