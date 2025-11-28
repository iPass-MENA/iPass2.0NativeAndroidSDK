package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.gson.JsonParser
import com.sdk.ipassplussdk.R
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.model.request.authentication.AuthenticationRequest
import com.sdk.ipassplussdk.model.request.create_aws_session.SessionCreateRequestNew
import com.sdk.ipassplussdk.model.request.initiate_data.UploadDataRequest
import com.sdk.ipassplussdk.model.response.authentication.AuthenticationResponse
import com.sdk.ipassplussdk.model.response.consumption.CustomerAccessResponse
import com.sdk.ipassplussdk.model.response.create_aws_session.SessionCreateResponseNew
import com.sdk.ipassplussdk.model.response.initiate_data.UploadDataResponse
import com.sdk.ipassplussdk.model.response.transaction_details.TransactionDetailResponse
import com.sdk.ipassplussdk.ui.DocumentReaderData
import com.sdk.ipassplussdk.ui.FaceScannerData.initFaceDetector
import com.sdk.ipassplussdk.utils.InternetConnectionService
import com.sdk.ipassplussdk.views.ProgressManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.HashMap
import java.util.Locale
import java.util.UUID


object iPassSDKManger {

    private var sid = ""
    private var rawResult: String? = "{}"


    //    authentication
    @RequiresApi(Build.VERSION_CODES.O)
    fun UserOnboardingProcess(
        context: Context,
        email: String?,
        password: String?,
        completion: ResultListener<AuthenticationResponse>,
    ) {

        if (!InternetConnectionService.networkAvailable(context)) {
            completion.onError(context.getString(R.string.internet_connection_not_found))
            return
        }

        if (email.isNullOrEmpty()) {
            completion.onError(context.getString(R.string.email_is_required))
            return
        }

        if (password.isNullOrEmpty()) {
            completion.onError(context.getString(R.string.password_is_required))
            return
        }

        ProgressManager.showProgress(context)
        val request = AuthenticationRequest(email, password)
        AuthData.auth(context, request, object : ResultListener<AuthenticationResponse> {
            override fun onSuccess(response: AuthenticationResponse?) {
                ProgressManager.dismissProgress()
                completion.onSuccess(response)
            }

            override fun onError(exception: String) {
                ProgressManager.dismissProgress()
                completion.onError(exception)
            }

        })
    }

    //    Show scanner for document verification
    @RequiresApi(Build.VERSION_CODES.O)
    fun startScanningProcess(
        context: Context,
        email: String?,
        userToken: String?,
        appToken: String?,
        socialMediaEmail: String = "",
        phoneNumber: String = "",
        flowId: String?,
        bindingView: ViewGroup?,
        callback: (status: Boolean, message: String) -> Unit
    ) {
        if (!InternetConnectionService.networkAvailable(context)) {
            callback.invoke(false, context.getString(R.string.internet_connection_not_found))
            return
        }
        if (email.isNullOrEmpty()) {
            callback.invoke(false, context.getString(R.string.email_is_required))
            return
        }
        if (userToken.isNullOrEmpty()) {
            callback.invoke(false, context.getString(R.string.user_token_is_required))
            return
        }
        if (appToken.isNullOrEmpty()) {
            callback.invoke(false, context.getString(R.string.app_token_is_required))
            return
        }
        if (flowId.isNullOrEmpty()) {
            callback.invoke(false, context.getString(R.string.flow_id_is_required))
            return
        }
        if ((flowId == "10031") && socialMediaEmail.isNullOrEmpty()) {
            callback.invoke(false, context.getString(R.string.social_media_email_is_required))
            return
        }
        if ((flowId == "10031") && phoneNumber.isNullOrEmpty()) {
            callback.invoke(false, context.getString(R.string.phone_number_is_required))
            return
        }
        if (bindingView == null) {
            callback.invoke(false, context.getString(R.string.binding_viewgroup_is_null))
            return
        }

        val currentLang = Locale.getDefault().language
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.getDefault().language
        }
        else{
            Locale.getDefault().language
        }


        ProgressManager.showProgress(context)
        Consumption.checkAccess(context, appToken, language = currentLang, object : ResultListener<CustomerAccessResponse> {
            override fun onSuccess(response: CustomerAccessResponse?) {
                if (response?.message.equals("sucess")) {
                    showDocScanner(context, appToken, userToken, email, socialMediaEmail, phoneNumber, flowId,  bindingView, callback)
                } else {
                    ProgressManager.dismissProgress()
                    callback.invoke(false, response?.message!!)
                }
            }

            override fun onError(exception: String) {
                ProgressManager.dismissProgress()
                callback.invoke(false, exception)
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDocScanner(
        context: Context,
        appToken: String,
        userToken: String,
        email: String,
        socialMediaEmail: String,
        phoneNumber: String,
        flowId: String,
        bindingView: ViewGroup,
        callback: (status: Boolean, message: String) -> Unit
    ) {
        sid = getSid()

        // Run everything in coroutine to ensure IP is fetched before proceeding
        CoroutineScope(Dispatchers.Main).launch {
            ProgressManager.showProgress(context)

            // Get public IP in IO thread
            val ip = withContext(Dispatchers.IO) {
                getPublicIpAddress()
            } ?: ""

            Log.e("call", "#### IP fetched: $ip")

       //     ProgressManager.dismissProgress()

            // Continue with scanner process
            DocumentReaderData.showScanner(context) { status, message ->
                if (status) {
                    rawResult = message
                    val source = "Android 2.19"

                    if (flowId == "10015" || flowId == "10016") {
                        uploadData(
                            context,
                            appToken,
                            email,
                            socialMediaEmail,
                            phoneNumber,
                            ip,
                            flowId,
                            source,
                            "0",
                            callback
                        )
                    } else {
                        faceSessionCreateRequest(
                            context,
                            email,
                            userToken,
                            appToken,
                            socialMediaEmail,
                            phoneNumber,
                            ip,
                            flowId,
                            source,
                            bindingView,
                            callback
                        )
                    }
                } else {
                    ProgressManager.dismissProgress()
                    callback.invoke(false, message)
                }
            }
        }
    }


    //    init face detection
    @RequiresApi(Build.VERSION_CODES.O)
    private fun faceSessionCreateRequest(
        context: Context,
        email: String,
        userToken: String,
        appToken: String,
        socialMediaEmail: String,
        phoneNumber: String,
        ipadd: String,
        flowId: String,
        source: String,
        bindingView: ViewGroup,
        callback: (Boolean, String) -> Unit
    ) {
        if (!InternetConnectionService.networkAvailable(context)) {
            return
        }

        val request = SessionCreateRequestNew()
        request.email = email
        request.authToken = userToken

        SessionCreateDataNew.createSession(
            context,
            appToken,
            request,
            object : ResultListener<SessionCreateResponseNew> {
                override fun onSuccess(response: SessionCreateResponseNew?) {
                    val sessionId = response?.sessionId!!

                    showFaceScanner(
                        context = context,
                        email = email,
                        userToken = userToken,
                        appToken = appToken,
                        socialMediaEmail = socialMediaEmail,
                        phoneNumber = phoneNumber,
                        ipadd = ipadd,
                        flowId = flowId,
                        source = source,
                        sessionId = sessionId,
                        bindingView = bindingView,
                        callback = callback
                    )

                }
                override fun onError(exception: String) {
                    ProgressManager.dismissProgress()
                    callback.invoke(false, exception)
                }
            })
    }

    //  show scanner to detect face liveness
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showFaceScanner(
        context: Context,
        email: String,
        userToken: String,
        appToken: String,
        socialMediaEmail: String,
        phoneNumber: String,
        ipadd: String,
        flowId: String,
        source: String,
        sessionId: String,
        bindingView: ViewGroup,
        callback: (Boolean, String) -> Unit
    ) {
        ProgressManager.dismissProgress()
        initFaceDetector(context, sessionId, bindingView) {
                    ProgressManager.showProgress(context)
                    uploadData(
                        context,
                        appToken,
                        email,
                        socialMediaEmail,
                        phoneNumber,
                        ipadd,
                        flowId,
                        source,
                        sessionId,
                        callback
                    )
    }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadData(
        context: Context,
        token: String,
        userEmail: String,
        socialMediaEmail: String,
        phoneNumber: String,
        ipadd: String,
        flowId: String,
        source: String,
        sessionId: String,
        callback: (Boolean, String) -> Unit
    ) {

        val getDeviceLanguage = Locale.getDefault().language

        val uploaddataRequest = UploadDataRequest()
        uploaddataRequest.email = userEmail
        uploaddataRequest.randomid = sid
        uploaddataRequest.socialMediaEmail = socialMediaEmail
        uploaddataRequest.phoneNumber= phoneNumber
        uploaddataRequest.ipadd= ipadd
        uploaddataRequest.workflow= flowId
        uploaddataRequest.source= source
        uploaddataRequest.language= getDeviceLanguage
        uploaddataRequest.sessionId = sessionId
        val rawData = JsonParser().parse(rawResult).asJsonObject
        uploaddataRequest.idvData = rawData

        InitiateData.uploadData(context, token,uploaddataRequest, object : ResultListener<UploadDataResponse> {
            override fun onSuccess(response: UploadDataResponse?) {
                ProgressManager.dismissProgress()
                callback.invoke(true, response?.message!!)
            }
            override fun onError(exception: String) {
                ProgressManager.dismissProgress()
                callback.invoke(false, exception)
            }
        })
    }



    //    returns a list of available Processing Scenarios
    fun getWorkFlows(): Array<HashMap<String, String>> {

        return Workflows.getList()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getDocumentScannerData(
        context: Context,
        appToken: String,
        completion: ResultListener<TransactionDetailResponse>) {
        if (!InternetConnectionService.networkAvailable(context)) {
            completion.onError(context.getString(R.string.internet_connection_not_found))
            return
        }
        if (appToken.isNullOrEmpty()) {
            completion.onError(context.getString(R.string.app_token_is_required))
            return
        }

        ProgressManager.showProgress(context)
        TransactionDetail.transactionDetails(context, appToken, sid, object : ResultListener<TransactionDetailResponse> {
            override fun onSuccess(response: TransactionDetailResponse?) {
                ProgressManager.dismissProgress()
                completion.onSuccess(response)
            }

            override fun onError(exception: String) {
                ProgressManager.dismissProgress()
                completion.onError(exception)
            }
        })
    }


    //    returns a unique sId for every scan
    private fun getSid(): String {
        // Generate a random UUID
        val myUuid = UUID.randomUUID()
        val myUuidAsString = myUuid.toString()

        return myUuidAsString
    }

//    suspend fun getPublicIpAddress(): String? = withContext(Dispatchers.IO) {
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("https://api.ipify.org")
//            .header("User-Agent", "Mozilla/5.0")
//            .build()
//
//        try {
//            client.newCall(request).execute().use { response ->
//                if (!response.isSuccessful) {
//                    throw IOException("Unexpected code $response")
//                }
//                response.body?.string()
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }

    suspend fun getPublicIpAddress(): String? = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.ipify.org?format=text") // ensure plain text response
            .header("User-Agent", "Mozilla/5.0")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body?.string()?.trim()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


}
