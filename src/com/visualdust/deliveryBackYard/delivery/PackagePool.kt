package com.visualdust.deliveryBackYard.delivery

class PackagePool {
    companion object {
        /**
         * for-short declarations
         */
        private var `receiverUID-key` = "ruid"
        private var `senderUID-key` = "suid"

        /**
         * Package searching pools
         */
        // For packageID-package searching
        public var `pid-pkg-Dictionary` = HashMap<String, PackageInfo>()
        // For senderUserID-packages searching
        public var `suid-pkgs-Dictionary` = HashMap<String, MutableList<PackageInfo>>()
        // For receiverUserID-packages searching
        public var `ruid-pkgs-Dictionary` = HashMap<String, MutableList<PackageInfo>>()

        /**
         * Package searching APIS
         */

        // When drop a package into pools
        fun drop(it: PackageInfo) {
            // Put it into the pid-pkg searching pool
            `pid-pkg-Dictionary`.put(it.getID(), it)

            // Put it into the suid-pkgs searching pool
            if (it.extension.checkIfThereIs(`senderUID-key`)) {// it has a sender
                // After the 'if' checque above, suid must be null-safe
                val suid = it.extension.getValueOf(`senderUID-key`)!!
                if (!`suid-pkgs-Dictionary`.containsKey(suid))
                // Create a pkg list for suid if there isn't any
                    `suid-pkgs-Dictionary`.put(suid, mutableListOf())
                `suid-pkgs-Dictionary`.getValue(suid).add(it)
            }

            // Put it into the ruid-pkgs searching pool
            if (it.extension.checkIfThereIs(`receiverUID-key`)) {// it has a receiver
                // After the 'if' checque above, suid must be null-safe
                val ruid = it.extension.getValueOf(`receiverUID-key`)!!
                if (!`ruid-pkgs-Dictionary`.containsKey(ruid))
                // Create a pkg list for ruid if there isn't any
                    `ruid-pkgs-Dictionary`.put(ruid, mutableListOf())
                `ruid-pkgs-Dictionary`.getValue(ruid).add(it)
            }
        }

        /**
         * When trying to find a package via packageID
         * Remove a package from all the pools if it's in the pid-pkg searching pool is not
         * a secure operation cause the package may don't have a sender or receiver.
         */
        fun findByPkgID(packageID: String): PackageInfo = `pid-pkg-Dictionary`.getValue(packageID)

        fun findBySuid(SenderUID: String): MutableList<PackageInfo> = `suid-pkgs-Dictionary`.getValue(SenderUID)

        fun findByRuid(ReceiverUID: String): MutableList<PackageInfo> = `ruid-pkgs-Dictionary`.getValue(ReceiverUID)

        // When trying to remove a package via packageID
        fun removeByPid(packageID: String): Boolean {
            var removedFlag = false
            // Remove it from the pid-pkg searching pool
            if (`pid-pkg-Dictionary`.containsKey(packageID)) {// package in pool
                var pkg = `pid-pkg-Dictionary`.getValue(packageID)
                if (pkg.extension.checkIfThereIs(`senderUID-key`))// if in suid-pkg searching pool is possible
                    `suid-pkgs-Dictionary`.remove(pkg.extension.getValueOf(`senderUID-key`))
                if (pkg.extension.checkIfThereIs(`receiverUID-key`))//if in ruid-pkg searching pool is possible
                    `ruid-pkgs-Dictionary`.remove(pkg.extension.getValueOf(`receiverUID-key`))
                //finally remove it from the pid-pkg searching pool
                `pid-pkg-Dictionary`.remove(packageID)
                removedFlag = true
            }
            return removedFlag
        }

        //When trying to remove a package via senderUID
        fun removeBySuid(senderUID: String): Boolean {
            var removeFlag = false
            if (`suid-pkgs-Dictionary`.containsKey(senderUID)) {// package in pool
                var pkgs = `suid-pkgs-Dictionary`.getValue(senderUID)
                for (pkg in pkgs) {// an user may related with many packages
                    if (`pid-pkg-Dictionary`.containsKey(pkg.getID()))// if in pid-pkg searching pool is possible
                        `pid-pkg-Dictionary`.remove(pkg.getID())
                    if (pkg.extension.checkIfThereIs(`receiverUID-key`))//if in ruid-pkg searching pool is possible
                        `ruid-pkgs-Dictionary`.remove(pkg.extension.getValueOf(`receiverUID-key`))
                }
                //finally remove it from the suid-pkg searching pool
                `suid-pkgs-Dictionary`.remove(senderUID)
                removeFlag = true
            }
            return removeFlag
        }

        //When trying to remove a package via receiverUID
        fun removeByRuid(receiverUID: String): Boolean {
            var removeFlag = false
            if (`ruid-pkgs-Dictionary`.containsKey(receiverUID)) {// package in pool
                var pkgs = `ruid-pkgs-Dictionary`.getValue(receiverUID)
                for (pkg in pkgs) {// an user may related with many packages
                    if (`pid-pkg-Dictionary`.containsKey(pkg.getID()))// if in pid-pkg searching pool is possible
                        `pid-pkg-Dictionary`.remove(pkg.getID())
                    if (pkg.extension.checkIfThereIs(`senderUID-key`))//if in suid-pkg searching pool is possible
                        `suid-pkgs-Dictionary`.remove(pkg.extension.getValueOf(`senderUID-key`))
                }
                //finally remove it from the suid-pkg searching pool
                `ruid-pkgs-Dictionary`.remove(receiverUID)
                removeFlag = true
            }
            return removeFlag
        }

    }
}