package com.sdk.ipassplussdk.model.response.authentication
import com.google.gson.annotations.SerializedName

data class User(
    val email: String,
    val token: String,
    val user_id: String,

    @SerializedName("customer_data")
    val customerData: CustomerData?
)