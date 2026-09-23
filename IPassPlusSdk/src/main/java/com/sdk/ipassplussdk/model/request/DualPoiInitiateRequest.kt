package com.sdk.ipassplussdk.model.request

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName


data class DualPoiInitiateRequest(

    @SerializedName("sessionId")
    var sessionId: String? = null,

    @SerializedName("workflow")
    var workflow: String? = null,

    @SerializedName("source")
    var source: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("id_document_data")
    var idDocumentData: JsonObject? = null,

    @SerializedName("passport_data")
    var passportData: JsonObject? = null,

    @SerializedName("liveness_data")
    var livenessData: JsonObject? = null,


)