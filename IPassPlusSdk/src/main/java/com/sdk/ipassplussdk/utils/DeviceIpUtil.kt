package com.sdk.ipassplussdk.utils

import java.net.Inet4Address
import java.net.NetworkInterface

object DeviceIpUtil {

    fun getLocalIpAddress(): String {
        return try {
            val interfaces = NetworkInterface.getNetworkInterfaces()

            for (networkInterface in interfaces) {

                val addresses = networkInterface.inetAddresses

                for (address in addresses) {

                    if (!address.isLoopbackAddress && address is Inet4Address) {

                        return address.hostAddress ?: ""
                    }
                }
            }

            ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}