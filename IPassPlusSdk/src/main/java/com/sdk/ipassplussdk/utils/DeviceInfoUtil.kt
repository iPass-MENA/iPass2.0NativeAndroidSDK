package com.sdk.ipassplussdk.utils

import android.os.Build

object DeviceInfoUtil {

    fun getDeviceType(): String {

        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }

    fun getAndroidVersion(): String {

        return "Android ${Build.VERSION.RELEASE}"
    }

    fun getDeviceBrand(): String {

        return Build.BRAND
    }

    fun getDeviceModel(): String {

        return Build.MODEL
    }
}