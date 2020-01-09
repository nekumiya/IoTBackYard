package com.visualdust.deliveryBackYard.mqttServerSide

import com.visualdust.deliveryBackYard.common.Resource
import com.visualdust.deliveryBackYard.common.Toolbox
import com.visualdust.deliveryBackYard.terminal.Command
import com.visualdust.deliveryBackYard.terminal.ITerminal
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

class TerminalMQTTSide : ITerminal<String> {

    var mqttClient: ServerSideMqttClient
    override var cmdMap = HashMap<String, Command<String>>()

    private var blankSize = 1;

    constructor(mqttClient: ServerSideMqttClient) {
        this.mqttClient = mqttClient


        /**
         * Adding commands
         */
        //Command "help"
        this.buildInCommand(Command("mqtt-help", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> print("TerminalMQTTSide: See what you'd like to do here:\n" +
                        "   [mqtt-connect]      : manually connect to the broker\n" +
                        "   [mqtt-disconnect]   : manually disconnect from the broker\n" +
                        "   [mqtt-subscribe]    : subscribe a topic\n" +
                        "   [mqtt-unsubscribe]  : unsubscribe a topic\n" +
                        "   [mqtt-publish]      : publish a message\n" +
                        "   [mqtt-status]       : check the status of mqtt client\n" + Resource.COMMAND_PROMPT)
                else -> {
                    print("Syntax error.\n" +
                            "Usage: mqtt-help\n" + Resource.COMMAND_PROMPT)
                }
            }
        }))

        //command "connect"
        this.buildInCommand(Command("mqtt-connect", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> mqttClient.connect(true)
                1 + blankSize -> {
                    mqttClient.`server-address` = argList[0 + blankSize]
                    mqttClient.connect()
                }
                2 + blankSize -> {
                    mqttClient.`server-address` = argList[0 + blankSize]
                    var option = MqttConnectOptions()
                    option.userName = argList[1 + blankSize]
                    mqttClient.connect(option)
                }
                3 + blankSize -> {
                    mqttClient.`server-address` = argList[0 + blankSize]
                    var option = MqttConnectOptions()
                    option.userName = argList[1 + blankSize]
                    option.password = argList[2 + blankSize].toCharArray()
                    mqttClient.connect(option)
                }
                else -> print("Syntax error.\n" +
                        "Usage: mqtt-connect [address] [username] [password]\n" +
                        "Keep stuffs in \"[]\" empty to use default connect option\n" + Resource.COMMAND_PROMPT)

            }
        }))

        //command "disconnect"
        this.buildInCommand(Command("mqtt-disconnect", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> mqttClient.disconnect()
                else -> print("Syntax error.\n" +
                        "Usage: mqtt-disconnect\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "reconnect"
        this.buildInCommand(Command("mqtt-reconnect", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> mqttClient.reconnect()
                else -> print("Syntax error.\n" +
                        "Usage: mqtt-reconnect\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "subscribe"
        this.buildInCommand(Command("mqtt-subscribe", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                1 + blankSize -> mqttClient.subscribeTopic(argList[0 + blankSize])
                else -> print("Syntax error.\n" +
                        "Usage: mqtt-subscribe [topic]\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "unsubscribe"
        this.buildInCommand(Command("mqtt-unsubscribe", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                1 + blankSize -> mqttClient.unsubscribeTopic(argList[0 + blankSize])
                else -> print("Syntax error.\n" +
                        "Usage: mqtt-unsubscribe [topic]\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "publish"
        this.buildInCommand(Command("mqtt-publish", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                2 + blankSize -> {
                    mqttClient.publish(argList[0 + blankSize], argList[1 + blankSize])
                }
                else -> print("Syntax error.\n" +
                        "Usage: mqtt-publish [message] [topic]\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "status"
        this.buildInCommand(Command("mqtt-status", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> {
                    print(mqttClient.readStatus(false) + "\n" + Resource.COMMAND_PROMPT)
                }
                else -> print("Syntax error.\n" +
                        "Usage: mqtt-status\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "exit"
//        this.buildInCommand(Command("exit", Consumer {
//            var argList = Toolbox.Split(it, " ", 0)
//            when (argList.size) {
//                0 + blankSize -> {
//                    EventRW.Write("${Resource.MQTTSIDE_NAME}->Terminal : Server exiting, operated by user.")
//                    System.exit(0)
//                }
//                else -> print("Syntax error.\n" +
//                        "Usage: exit\n"+Resource.COMMAND_PROMPT)
//            }
//        }))
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
            print("Command not found. Why not ask for \"mqtt-help\" ?\n" + Resource.COMMAND_PROMPT)
        }
    }

    override fun start() {
        ScannerThread(this).start()
    }

    internal class ScannerThread(var terminalMQTTSide: TerminalMQTTSide) : Thread() {
        var scanner = Scanner(System.`in`)

        override fun run() {
            while (true) {
                var userInput = scanner.nextLine()
                terminalMQTTSide.run(userInput)
            }
        }
    }
}