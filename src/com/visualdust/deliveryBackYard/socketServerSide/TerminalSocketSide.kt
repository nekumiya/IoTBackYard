package com.visualdust.deliveryBackYard.socketServerSide

import com.visualdust.deliveryBackYard.common.Toolbox
import com.visualdust.deliveryBackYard.mqttServerSide.TerminalMQTTSide
import com.visualdust.deliveryBackYard.terminal.Command
import com.visualdust.deliveryBackYard.terminal.ITerminal
import java.util.*
import kotlin.collections.HashMap

class TerminalSocketSide : ITerminal<String> {
    override var cmdMap: HashMap<String, Command<String>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    private var blankSize = 1

    constructor() {

        /**
         * Adding commands
         */
        //todo finish constructor
    }

    override fun buildInCommand(command: Command<String>) {
        cmdMap.put(command.name, command)
    }

    override fun run(command: String) {
        var splitedCmd = Toolbox.Split(command, " ", 0)
        var key = splitedCmd.elementAt(0)
        if (cmdMap.containsKey(key)) {
            cmdMap.getValue(key).resolve(command.substring(key.length))
        } else {
            print("Command not found. Why not ask for \"socket-help\" ?\n>>>")
        }
    }

    override fun start() {
        ScannerThread(this).start()
    }

    internal class ScannerThread(var terminalSocketSide: TerminalSocketSide) : Thread() {
        var scanner = Scanner(System.`in`)

        override fun run() {
            while (true) {
                var userInput = scanner.nextLine()
                terminalSocketSide.run(userInput)
            }
        }
    }
}