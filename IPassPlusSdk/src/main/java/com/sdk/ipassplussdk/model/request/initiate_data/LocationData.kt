package com.sdk.ipassplussdk.model.request.initiate_data

import com.google.gson.annotations.SerializedName

data class LocationData(
    @SerializedName("latitude")
    var latitude: Double? = null,

    @SerializedName("longitude")
    var longitude: Double? = null,

    @SerializedName("accuracy")
    var accuracy: Float? = null,

    @SerializedName("locationSource")
    var locationSource: String? = null,

    @SerializedName("permissionStatus")
    var permissionStatus: String? = null,

    @SerializedName("ipAddress")
    var ipAddress: String? = null,

    @SerializedName("capturedAt")
    var capturedAt: String? = null,
)
