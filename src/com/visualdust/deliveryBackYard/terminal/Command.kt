package com.visualdust.deliveryBackYard.terminal

import java.util.function.Consumer

class Command<T> {
    public var name = ""
    var resolver: Consumer<T>

    constructor(name: String, resolver: Consumer<T>) {
        this.name = name
        this.resolver = resolver
    }

    fun resolve(obj: T) = resolver.accept(obj)
}