package com.sdk.ipassplussdk.model.response.authentication

import com.google.gson.annotations.SerializedName

data class CustomerData(

    @SerializedName("docScan_suboptions")
    val docScanSuboptions: DocScanSuboptions?,

    @SerializedName("_id")
    val id: String?,

    @SerializedName("companyname")
    val companyName: String?,

    @SerializedName("firstname")
    val firstName: String?,

    @SerializedName("lastname")
    val lastName: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("logo")
    val logo: String?,

    @SerializedName("accountType")
    val accountType: String?,

    @SerializedName("billsEmail")
    val billsEmail: String?,

    @SerializedName("isSupAdmin")
    val isSupAdmin: String?,

    @SerializedName("redirect_url")
    val redirectUrl: String?,

    @SerializedName("webhook_url")
    val webhookUrl: String?,

    @SerializedName("idv_webhook_url")
    val idvWebhookUrl: String?,

    @SerializedName("aml_webhook_url")
    val amlWebhookUrl: String?,

    @SerializedName("creat_at")
    val createdAt: String?,

    @SerializedName("token")
    val customerToken: String?,

    @SerializedName("issuspended")
    val isSuspended: String?,

    @SerializedName("forgotLinkTime")
    val forgotLinkTime: String?,

    @SerializedName("is_onprem")
    val isOnPrem: Boolean?,

    @SerializedName("is_vpn")
    val isVpn: Boolean?,

    @SerializedName("tms_access")
    val tmsAccess: String?,

    @SerializedName("tms_limit")
    val tmsLimit: Int?,

    @SerializedName("tms_remaining")
    val tmsRemaining: Int?,

    @SerializedName("aml_access")
    val amlAccess: String?,

    @SerializedName("aml_limit")
    val amlLimit: Int?,

    @SerializedName("aml_remaining")
    val amlRemaining: Int?,

    @SerializedName("docScan_access")
    val docScanAccess: String?,

    @SerializedName("docScan_limit")
    val docScanLimit: Int?,

    @SerializedName("docScan_remaining")
    val docScanRemaining: Int?,

    @SerializedName("idv_access")
    val idvAccess: String?,

    @SerializedName("idv_limit")
    val idvLimit: Int?,

    @SerializedName("idv_remaining")
    val idvRemaining: Int?,

    @SerializedName("dualpoi_access")
    val dualPoiAccess: String?,

    @SerializedName("dualpoi_limit")
    val dualPoiLimit: Int?,

    @SerializedName("dualpoi_consumed")
    val dualPoiConsumed: Int?,

    @SerializedName("dualpoi_remaining")
    val dualPoiRemaining: Int?,

    @SerializedName("domain")
    val domain: String?,

    @SerializedName("DocAuth_access")
    val docAuthAccess: String?,

    @SerializedName("DocAuth_limit")
    val docAuthLimit: Int?,

    @SerializedName("DocAuth_remaining")
    val docAuthRemaining: Int?,

    @SerializedName("kyc_access")
    val kycAccess: String?,

    @SerializedName("kyc_limit")
    val kycLimit: Int?,

    @SerializedName("kyc_consumed")
    val kycConsumed: Int?,

    @SerializedName("kyc_remaining")
    val kycRemaining: Int?,

    @SerializedName("LicenseActivated")
    val licenseActivated: Boolean?,

    @SerializedName("is_active")
    val isActive: Boolean?,

    @SerializedName("update_at")
    val updatedAt: String?,

    @SerializedName("__v")
    val version: Int?,

    @SerializedName("idv_consumed")
    val idvConsumed: Int?,

    @SerializedName("dualpoi_enabled")
    val dualPoiEnabled: Boolean?,

    @SerializedName("kyc_dualpoi_access")
    val kycDualPoiAccess: String?,

    @SerializedName("kyc_dualpoi_limit")
    val kycDualPoiLimit: Int?,

    @SerializedName("kyc_dualpoi_remaining")
    val kycDualPoiRemaining: Int?
)

data class DocScanSuboptions(

    @SerializedName("translate")
    val translate: Boolean?,

    @SerializedName("search")
    val search: Boolean?,

    @SerializedName("crop")
    val crop: Boolean?,

    @SerializedName("enhance")
    val enhance: Boolean?,

    @SerializedName("ocr")
    val ocr: Boolean?
)