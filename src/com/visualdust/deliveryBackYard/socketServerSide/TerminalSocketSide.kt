package com.visualdust.deliveryBackYard.socketServerSide

import com.visualdust.deliveryBackYard.terminal.Command
import com.visualdust.deliveryBackYard.terminal.ITerminal

class TerminalSocketSide : ITerminal<String> {
    override var cmdMap: HashMap<String, Command<String>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun buildInCommand(command: Command<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun run(command: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}