package com.visualdust.deliveryBackYard.socketServerSide

import com.visualdust.deliveryBackYard.common.Resource
import com.visualdust.deliveryBackYard.common.Toolbox
import com.visualdust.deliveryBackYard.mqttServerSide.TerminalMQTTSide
import com.visualdust.deliveryBackYard.terminal.Command
import com.visualdust.deliveryBackYard.terminal.ITerminal
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

class TerminalSocketSide : ITerminal<String> {
    override var cmdMap: HashMap<String, Command<String>> = HashMap()
        get() = field

    private var blankSize = 1

    constructor() {

        /**
         * Adding commands
         */
        //command "help"
        this.buildInCommand(Command("socket-help", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> print("TerminalSocketSide: See what you'd like to do here:\n" +
                        "   [socket-list]       : list out all the connected list\n" +
                        "   [socket-disconnect] : manually disconnect from a client\n" +
                        "   [mqtt-send]         : manually send a message to a client\n" +
                        "   [mqtt-unsubscribe]  : unsubscribe a topic\n" +
                        "   [socket-status]     : check the status of mqtt client\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "socket-list"
        this.buildInCommand(Command("socket-list", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0+blankSize->{
                    //todo finish this function
                }
            }
        }))
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
            print("Command not found. Why not ask for \"socket-help\" ?\n" + Resource.COMMAND_PROMPT)
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