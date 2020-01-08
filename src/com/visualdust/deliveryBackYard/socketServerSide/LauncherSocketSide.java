package com.visualdust.deliveryBackYard.socketServerSide;

import com.visualdust.deliveryBackYard.common.EventRW;

public class LauncherSocketSide {
    public static void main(String[] args) {
        Launch();
    }

    public static void Launch() {
        EventRW.Write("---<---BackyardMQTTSide starting up......--->---");
    }

}
