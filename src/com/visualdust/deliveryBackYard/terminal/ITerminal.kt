package com.visualdust.deliveryBackYard.terminal

interface ITerminal<T> {
    var cmdMap: HashMap<String, Command<T>>
    fun buildInCommand(command: Command<T>)
    fun run(command: String)
    fun start()
}