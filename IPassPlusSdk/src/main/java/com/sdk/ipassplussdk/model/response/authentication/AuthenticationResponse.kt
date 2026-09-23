package com.sdk.ipassplussdk.model.response.authentication
import com.google.gson.annotations.SerializedName

//data class AuthenticationResponse(
//    val user: User
//)

data class AuthenticationResponse(
    @SerializedName("user")
    val user: User
)



