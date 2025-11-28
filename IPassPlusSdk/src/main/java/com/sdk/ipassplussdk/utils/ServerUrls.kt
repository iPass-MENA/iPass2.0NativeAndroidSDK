package com.sdk.ipassplussdk.utils

object ServerUrls {
//    live
    const val base_url = "https://plusapi.ipass-mena.com/api/v1/ipass/"
    // stage url
//    const val base_url = "https://staging.ipass-mena.com/stagapi/api/v1/ipass/"

   // test url
//  const val base_url = "http://192.168.11.27:4088/api/v1/ipass/"
//  const val base_url = "http://192.168.11.48:4088/api/v1/ipass/"

//    local
//    const val base_url = "http://192.168.14.122/node/api/v1/ipass/"
//    const val base_url = "http://192.168.11.48:4088/api/v1/ipass/"
//    const val base_url = "http://192.168.11.48:4088/api/v1/ipass/"
    //  const val base_url = "https://ipassplus.csdevhub.com/api/v1/ipass/"

    const val url_authentication = "create/authenticate/login"
    const val url_customer_access = "customer/access"
    const val url_aws_session_create = "plus/face/session/create"
    const val url_upload_data = "initiate/process/sdk"
    const val url_transaction_detail = "idv/getAll/data"
}
