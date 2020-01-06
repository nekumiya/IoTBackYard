package com.visualdust.deliveryBackYard.mqttclient

import com.visualdust.deliveryBackYard.commomn.EventRW
import com.visualdust.deliveryBackYard.commomn.Toolbox
import com.visualdust.deliveryBackYard.terminal.Command
import com.visualdust.deliveryBackYard.terminal.ITerminal
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

class MqttServerSideTerminal : ITerminal<String> {

    var mqttClient: ServerSideMqttClient
    override var cmdMap = HashMap<String, Command<String>>()

    private var blankSize = 1;

    constructor(mqttClient: ServerSideMqttClient) {
        this.mqttClient = mqttClient


        /**
         * Adding commands
         */
        //Command "help"
        this.buildInCommand(Command("help", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> println("Help: See what you'd like to do here:\n" +
                        "   [connect]      : manual connect to the broker\n" +
                        "   [disconnect]   : manual disconnect from the broker\n" +
                        "   [subscribe]    : subscribe a topic\n" +
                        "   [unsubscribe]  : unsubscribe a topic\n" +
                        "   [publish]      : publish a message\n" +
                        "   [status]       : check the status of mqtt client\n>>>")
                else -> {
                    print("Syntax error.\n" +
                            "Usage: help\n>>>")
                }
            }
        }))

        //command "connect"
        this.buildInCommand(Command("connect", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                1 + blankSize -> mqttClient.connect(true)
//                2 + blankSize -> {
//                    mqttClient.`server-address` = argList[1]
//                    mqttClient.connect()
//                }
//                3 + blankSize -> {
//                    mqttClient.`server-address` = argList[1]
//                    var option = MqttConnectOptions()
//                    option.userName = argList[2]
//                    mqttClient.connect(option)
//                }
//                4 + blankSize -> {
//                    mqttClient.`server-address` = argList[1]
//                    var option = MqttConnectOptions()
//                    option.userName = argList[2]
//                    option.password = argList[3].toCharArray()
//                    mqttClient.connect(option)
//                }
                else -> print("Syntax error.\n" +
                        "Usage: connect [address] [username] [password]\n" +
                        "Keep stuffs in \"[]\" empty to use default connect option\n>>>")

            }
        }))

        //command "disconnect"
        this.buildInCommand(Command("disconnect", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> mqttClient.disconnect()
                else -> print("Syntax error.\n" +
                        "Usage: disconnect\n>>>")
            }
        }))

        //command "reconnect"
        this.buildInCommand(Command("reconnect", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> mqttClient.reconnect()
                else -> print("Syntax error.\n" +
                        "Usage: reconnect\n>>>")
            }
        }))

        //command "subscribe"
        this.buildInCommand(Command("subscribe", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                1 + blankSize -> mqttClient.subscribeTopic(argList[0])
                else -> print("Syntax error.\n" +
                        "Usage: subscribe [topic]\n>>>")
            }
        }))

        //command "unsubscribe"
        this.buildInCommand(Command("unsubscribe", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                1 + blankSize -> mqttClient.unsubscribeTopic(argList[0])
                else -> print("Syntax error.\n" +
                        "Usage: unsubscribe [topic]\n>>>")
            }
        }))

        //command "publish"
        this.buildInCommand(Command("publish", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                2 + blankSize -> {
                    mqttClient.publish(argList[1], argList[2])
                }
                else -> print("Syntax error.\n" +
                        "Usage: publish [message] [topic]\n>>>")
            }
        }))

        //command "status"
        this.buildInCommand(Command("status", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> {
                    print(mqttClient.readStatus(false) + "\n>>>")
                }
                else -> print("Syntax error.\n" +
                        "Usage: status\n>>>")
            }
        }))

        //command "exit"
        this.buildInCommand(Command("exit", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> {
                    EventRW.Write("Server exiting....")
                    System.exit(0)
                }
                else -> print("Syntax error.\n" +
                        "Usage: exit\n>>>")
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
            print("Command not found. Why not ask for \"help\" ?\n>>>")
        }
    }

    override fun start() {
        ScannerThread(this).start()
    }

    internal class ScannerThread(var mqttServerSideTerminal: MqttServerSideTerminal) : Thread() {
        var scanner = Scanner(System.`in`)

        override fun run() {
            while (true) {
                var userInput = scanner.nextLine()
                mqttServerSideTerminal.run(userInput)
            }
        }
    }
}