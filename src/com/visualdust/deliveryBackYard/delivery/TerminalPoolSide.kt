package com.visualdust.deliveryBackYard.delivery

import com.visualdust.deliveryBackYard.common.EventRW
import com.visualdust.deliveryBackYard.common.Resource
import com.visualdust.deliveryBackYard.common.Toolbox
import com.visualdust.deliveryBackYard.terminal.Command
import com.visualdust.deliveryBackYard.terminal.ITerminal
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

class TerminalPoolSide : ITerminal<String> {
    override var cmdMap: HashMap<String, Command<String>> = HashMap()
        get() = field
        set(value) {}

    private var blankSize = 1

    constructor() {
        /**
         * Adding commands
         */
        //Command "help"
        this.buildInCommand(Command("pool-help", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 + blankSize -> println("TerminalPoolSide: See what you'd like to do here:\n" +
                        "   [pool-list]         : list all pkgs in a searching pool\n" +
                        "   [pool-drop]         : drop a package into a searching pool manually\n" +
                        "   [pool-remove]       : remove a package in a pool by it's searching key\n" +
                        "   [pool-search]       : to search for a package in a pool via it's searching key\n" +
                        "   [pool-status]       : check the status of a pool\n" + Resource.COMMAND_PROMPT)
                else -> {
                    print("Syntax error.\n" +
                            "   Usage: pool-help\n" + Resource.COMMAND_PROMPT)
                }
            }
        }))

        //command "list"
        this.buildInCommand(Command("pool-list", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                1 + blankSize -> {
                    when (argList[0+blankSize]) {
                        "pid-pkg" -> {
                            print("---<---[${PackagePool.`pid-pkg-Dictionary`.size}] pkgs in pid-pkg searching pool--->---\n")
                            for (pkg in PackagePool.`pid-pkg-Dictionary`.values)
                                print(" " + pkg.toString() + "\n")
                            print("\n" + Resource.COMMAND_PROMPT)
                        }
                        "suid-pkg" -> {
                            print("---<---[${PackagePool.`suid-pkgs-Dictionary`.size}] pkgs in suid-pkg searching pool--->---\n")
                            for (pkg in PackagePool.`suid-pkgs-Dictionary`.values)
                                print(" " + pkg.toString() + "\n")
                            print("\n" + Resource.COMMAND_PROMPT)
                        }
                        "ruid-pkg" -> {
                            print("---<---[${PackagePool.`ruid-pkgs-Dictionary`.size}] pkgs in ruid-pkg searching pool--->---\n")
                            for (pkg in PackagePool.`ruid-pkgs-Dictionary`.values)
                                print(" " + pkg.toString() + "\n")
                            print("\n" + Resource.COMMAND_PROMPT)
                        }
                    }
                }
                else -> print("Syntax error.\n" +
                        "   Usage: pool-list [poolName]\n" +
                        "   Possible [poolName] : pid-pkg / suid-pkg / ruid-pkg\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "pool-drop"
        this.buildInCommand(Command("pool-drop", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                1 + blankSize -> {
                    var pkgInfo = PackageInfo(argList[0+blankSize])
                    if (pkgInfo.getID() != "null") {
                        PackagePool.drop(pkgInfo)
                        EventRW.WriteAsRichText(true, this.toString(), "Successfully initialized a package $pkgInfo and dropped it into the pool.")

                    } else print("[X]Could not create a package using arg. Is your input formatted?\n" + Resource.COMMAND_PROMPT)
                }
                else -> print("Syntax error.\n" +
                        "   Usage: pool-drop [packageInfo]\n" +
                        "   [packageInfo] must be a formatted string for initialize a package in-standard\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "pool-remove"
        this.buildInCommand(Command("pool-remove", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                2 + blankSize -> when (argList[0+blankSize]) {
                    "pid-pkg" -> {
                        if (PackagePool.removeByPid(argList[1+blankSize]))
                            EventRW.WriteAsRichText(true, this.toString(), "Package has been successfully removed")
                        else print("[X]Sorry, there is nothing match ${argList[1+blankSize]} in pool ${argList[0+blankSize]}")
                    }
                    "suid-pkg" -> {
                        if (PackagePool.removeBySuid(argList[1+blankSize]))
                            EventRW.WriteAsRichText(true, this.toString(), "Package has been successfully removed")
                        else print("[X]Sorry, there is nothing match ${argList[1+blankSize]} in pool ${argList[0+blankSize]}")
                    }
                    "ruid-pkg" -> {
                        if (PackagePool.removeByRuid(argList[1+blankSize]))
                            EventRW.WriteAsRichText(true, this.toString(), "Package has been successfully removed")
                        else print("[X]Sorry, there is nothing match ${argList[1+blankSize]} in pool ${argList[0+blankSize]}")
                    }
                    else -> print("[X]Sorry, there are no such pool named \"${argList[0+blankSize]}\"")
                }
                else -> print("Syntax error.\n" +
                        "   Usage: pool-remove [poolName] [key]\n" +
                        "   There are 3 possible instructions now:" +
                        "       1 pool-remove [pid-pkg]   [packageID]\n" +
                        "       2 pool-remove [suid-pkg]  [senderUID]\n" +
                        "       3 pool-remove [ruid-pkg]  [receiverUID]\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "pool-search"
        this.buildInCommand(Command("pool-search", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                1 -> {
                    var findFlag = false
                    if (PackagePool.`pid-pkg-Dictionary`.containsKey(argList[0+blankSize])) {
                        print("Package found in pid-pkg searching pool : ${PackagePool.findByPkgID(argList[0+blankSize])}\n")
                        findFlag = true
                    }
                    if (PackagePool.`suid-pkgs-Dictionary`.containsKey(argList[0+blankSize])) {
                        print("Package found in suid-pkg searching pool : ${PackagePool.findBySuid(argList[0+blankSize])}\n")
                        findFlag = true
                    }
                    if (PackagePool.`ruid-pkgs-Dictionary`.containsKey(argList[0+blankSize])) {
                        print("Package found in ruid-pkg searching pool : ${PackagePool.findByRuid(argList[0+blankSize])}\n")
                        findFlag = true
                    }
                    if (!findFlag) print("[X]Could not find \"${argList[0+blankSize]}\" in any pool\n")
                    print(Resource.COMMAND_PROMPT)
                }
                2 -> {
                    when (argList[1]) {
                        "pid-pkg" -> if (PackagePool.`pid-pkg-Dictionary`.containsKey(argList[0+blankSize])) {
                            print("Package found in ${argList[1+blankSize]} searching pool : ${PackagePool.findByPkgID(argList[0+blankSize])}\n")
                        } else print("[X]Could not find ${argList[0+blankSize]} in pool ${argList[1+blankSize]}\n")
                        "suid-pkg" -> if (PackagePool.`suid-pkgs-Dictionary`.containsKey(argList[0])) {
                            print("Package found in ${argList[1+blankSize]} searching pool : ${PackagePool.findBySuid(argList[0+blankSize])}\n")
                        } else print("[X]Could not find ${argList[0+blankSize]} in pool ${argList[1+blankSize]}\n")
                        "ruid-pkg" -> if (PackagePool.`ruid-pkgs-Dictionary`.containsKey(argList[0+blankSize])) {
                            print("Package found in ${argList[1+blankSize]} searching pool : ${PackagePool.findByRuid(argList[0+blankSize])}\n")
                        } else print("[X]Could not find ${argList[0+blankSize]} in pool ${argList[1+blankSize]}\n")
                        else -> print("[X]Pool named \"${argList[0+blankSize]}\" not found.\n" +
                                "   Possible [poolName] : pid-pkg / suid-pkg / ruid-pkg\n" + Resource.COMMAND_PROMPT)
                    }
                    print(Resource.COMMAND_PROMPT)
                }
                else -> print("Syntax error.\n" +
                        "   Usage: pool-search [id:Any] [poolName]\n" +
                        "   Possible [poolName] : pid-pkg / suid-pkg / ruid-pkg\n" +
                        "   [poolName] can be absent, and that way terminal will search [id] in all the possible pools\n" + Resource.COMMAND_PROMPT)
            }
        }))

        //command "pool-status"
        this.buildInCommand(Command("pool-status", Consumer {
            var argList = Toolbox.Split(it, " ", 0)
            when (argList.size) {
                0 -> {
                    print("---<---Status of pid-pkg searching pool--->---\n")
                    print("[${PackagePool.`pid-pkg-Dictionary`.size}] item(s) in ${PackagePool.`pid-pkg-Dictionary`}\n\n")
                    print("---<---Status of suid-pkg searching pool--->---\n")
                    print("[${PackagePool.`suid-pkgs-Dictionary`.size}] item(s) in ${PackagePool.`suid-pkgs-Dictionary`}\n\n")
                    print("---<---Status of ruid-pkg searching pool--->---\n")
                    print("[${PackagePool.`ruid-pkgs-Dictionary`.size}] item(s) in ${PackagePool.`ruid-pkgs-Dictionary`}\n\n")
                    print(Resource.COMMAND_PROMPT)
                }
                1 -> when (argList[0+blankSize]) {
                    "pid-pkg" -> {
                        print("---<---Status of pid-pkg searching pool--->---\n")
                        print("[${PackagePool.`pid-pkg-Dictionary`.size}] item(s) in ${PackagePool.`pid-pkg-Dictionary`}\n\n")
                        print(Resource.COMMAND_PROMPT)
                    }
                    "suid-pkg" -> {
                        print("---<---Status of suid-pkg searching pool--->---\n")
                        print("[${PackagePool.`suid-pkgs-Dictionary`.size}] item(s) in ${PackagePool.`suid-pkgs-Dictionary`}\n\n")
                        print(Resource.COMMAND_PROMPT)
                    }
                    "ruid-pkg" -> {
                        print("---<---Status of ruid-pkg searching pool--->---\n")
                        print("[${PackagePool.`ruid-pkgs-Dictionary`.size}] item(s) in ${PackagePool.`ruid-pkgs-Dictionary`}\n\n")
                        print(Resource.COMMAND_PROMPT)
                    }
                }
                else -> print("Syntax error.\n" +
                        "   Usage: pool-status [poolName]\n" +
                        "   Possible [poolName] : pid-pkg / suid-pkg / ruid-pkg\n" +
                        "   Keep [poolName empty if you want to see status of all the pools]\n" + Resource.COMMAND_PROMPT)
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
            print("Command not found. Why not ask for \"pool-help\" ?\n" + Resource.COMMAND_PROMPT)
        }
    }

    override fun start() {
        ScannerThread(this).start()
    }

    internal class ScannerThread(var terminalPoolSide: TerminalPoolSide) : Thread() {
        var scanner = Scanner(System.`in`)

        override fun run() {
            while (true) {
                var userInput = scanner.nextLine()
                terminalPoolSide.run(userInput)
            }
        }
    }
}