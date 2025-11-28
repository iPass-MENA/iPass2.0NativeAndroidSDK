package com.sdk.ipassplussdk.model.request.initiate_data

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class UploadDataRequest (


    @SerializedName("sessionId"          ) var sessionId        : String?  = null,
    @SerializedName("randomid"           ) var randomid         : String?  = null,
    @SerializedName("social_media_email" ) var socialMediaEmail : String?  = null,
    @SerializedName("phone_number"       ) var phoneNumber      : String?  = null,
    @SerializedName("ipadd"              ) var ipadd            : String?  = null,
    @SerializedName("email"              ) var email            : String?  = null,
    @SerializedName("workflow"           ) var workflow         : String?  = null,
    @SerializedName("source"             ) var source           : String?  = null,
    @SerializedName("language"           ) var language         : String?  = null,
    @SerializedName("idv_data"           ) var idvData          : JsonObject? = null,
    @SerializedName("jonfcData"           ) var jonfcData          : JsonObject? = null,

)
