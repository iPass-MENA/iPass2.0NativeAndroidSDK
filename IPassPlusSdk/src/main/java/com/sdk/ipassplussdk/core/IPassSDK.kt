package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.JsonParser
import com.sdk.ipassplussdk.R
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.model.request.DualPoiInitiateRequest
import com.sdk.ipassplussdk.model.request.authentication.AuthenticationRequest
import com.sdk.ipassplussdk.model.request.create_aws_session.SessionCreateRequestNew
import com.sdk.ipassplussdk.model.request.initiate_data.UploadDataRequest
import com.sdk.ipassplussdk.model.response.DualPoiInitiateResponse
import com.sdk.ipassplussdk.model.response.authentication.AuthenticationResponse
import com.sdk.ipassplussdk.model.response.consumption.CustomerAccessResponse
import com.sdk.ipassplussdk.model.response.create_aws_session.SessionCreateResponseNew
import com.sdk.ipassplussdk.model.response.initiate_data.UploadDataResponse
import com.sdk.ipassplussdk.model.response.transaction_details.TransactionDetailResponse
import com.sdk.ipassplussdk.ui.DocumentReaderData
import com.sdk.ipassplussdk.ui.FaceScannerData.initFaceDetector
import com.sdk.ipassplussdk.utils.DeviceInfoUtil
import com.sdk.ipassplussdk.utils.DeviceIpUtil
import com.sdk.ipassplussdk.utils.InternetConnectionService
import com.sdk.ipassplussdk.utils.VPNDetector
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
import com.google.gson.JsonObject
import com.sdk.ipassplussdk.model.response.authentication.CustomerData
import com.sdk.ipassplussdk.model.response.transaction_details.DualPoiTransactionResponse

object iPassSDKManger {

    private var sid = ""
    private var rawResult: String? = "{}"
    private var dualPoiSessionId = ""
    // Dual POI Implementation storage variables
    var passportRawResult: String? = null
    var idDocumentRawResult: String? = null

    private var customerData: CustomerData? = null
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
                customerData = response?.user?.customerData

                completion.onSuccess(response)
            }

            override fun onError(exception: String) {
                ProgressManager.dismissProgress()
                completion.onError(exception)
            }

        })
    }

//    fun isDualPoiAccessEnabled(): Boolean {
//        return customerData?.dualPoiAccess
//            ?.equals("true", ignoreCase = true) == true
//    }
//
//    fun hasDualPoiRemaining(): Boolean {
//        return (customerData?.dualPoiRemaining ?: 0) > 0
//    }

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

        // Phone IP address and Device Type
        val deviceType = DeviceInfoUtil.getDeviceType()
        val localIp = DeviceIpUtil.getLocalIpAddress()

        Log.e("call", "#### Local Device IP: $localIp")
        Log.e("call", "#### Device Type: $deviceType")

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.getDefault().language
        } else {
            Locale.getDefault().language
        }


        ProgressManager.showProgress(context)
        Consumption.checkAccess(
            context,
            appToken,
            language = currentLang,
            object : ResultListener<CustomerAccessResponse> {
                override fun onSuccess(response: CustomerAccessResponse?) {
                    if (response?.message.equals("sucess")) {

                        val vpnStatus = response?.is_vpn ?: false

                        // CHECK VPN ONLY IF ENABLED FROM BACKEND
                        if (vpnStatus && VPNDetector.isVpnActive(context)) {

                            ProgressManager.dismissProgress()

                            showVpnBlockedPopup(context)

                            callback.invoke(false, "VPN usage is not allowed.")

                            return
                        }

                        // Continue scanner flow
                        showDocScanner(
                            context,
                            appToken,
                            userToken,
                            email,
                            socialMediaEmail,
                            phoneNumber,
                            flowId,
                            bindingView,
                            callback
                        )
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

    // =========================================================================
    // DUAL POI IMPLEMENTATION
    // =========================================================================

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun startDualPoiScanningProcess(
//        context: Context,
//        email: String?,
//        userToken: String?,
//        appToken: String?,
//        socialMediaEmail: String = "",
//        phoneNumber: String = "",
//        flowId: String?,
//        bindingView: ViewGroup?,
//        callback: (status: Boolean, message: String) -> Unit
//    ) {
//        val deviceType = DeviceInfoUtil.getDeviceType()
//        val localIp = DeviceIpUtil.getLocalIpAddress()
//
//        Log.e("call", "#### Dual POI Local Device IP: $localIp")
//        Log.e("call", "#### Dual POI Device Type: $deviceType")
//
//        if (!InternetConnectionService.networkAvailable(context)) {
//            callback.invoke(false, context.getString(R.string.internet_connection_not_found))
//            return
//        }
//        if (email.isNullOrEmpty()) {
//            callback.invoke(false, context.getString(R.string.email_is_required))
//            return
//        }
//        if (userToken.isNullOrEmpty()) {
//            callback.invoke(false, context.getString(R.string.user_token_is_required))
//            return
//        }
//        if (appToken.isNullOrEmpty()) {
//            callback.invoke(false, context.getString(R.string.app_token_is_required))
//            return
//        }
//        if (flowId.isNullOrEmpty()) {
//            callback.invoke(false, context.getString(R.string.flow_id_is_required))
//            return
//        }
//        if ((flowId == "10031") && socialMediaEmail.isNullOrEmpty()) {
//            callback.invoke(false, context.getString(R.string.social_media_email_is_required))
//            return
//        }
//        if ((flowId == "10031") && phoneNumber.isNullOrEmpty()) {
//            callback.invoke(false, context.getString(R.string.phone_number_is_required))
//            return
//        }
//        if (bindingView == null) {
//            callback.invoke(false, context.getString(R.string.binding_viewgroup_is_null))
//            return
//        }
//
//        val currentLang = Locale.getDefault().language
//
//        ProgressManager.showProgress(context)
//        Consumption.checkAccess(
//            context,
//            appToken,
//            language = currentLang,
//            object : ResultListener<CustomerAccessResponse> {
//                override fun onSuccess(response: CustomerAccessResponse?) {
//                    if (response?.message.equals("sucess")) {
//                        val vpnStatus = response?.is_vpn ?: false
//
//                        if (vpnStatus && VPNDetector.isVpnActive(context)) {
//                            ProgressManager.dismissProgress()
//                            showVpnBlockedPopup(context)
//                            callback.invoke(false, "VPN usage is not allowed.")
//                            return
//                        }
//
//                        // Continue with Dual POI document scanning
//                        startDualPoiDocumentScanning(
//                            context,
//                            appToken,
//                            userToken,
//                            email,
//                            socialMediaEmail,
//                            phoneNumber,
//                            flowId,
//                            bindingView,
//                            callback
//                        )
//                    } else {
//                        ProgressManager.dismissProgress()
//                        callback.invoke(false, response?.message!!)
//                    }
//                }
//
//                override fun onError(exception: String) {
//                    ProgressManager.dismissProgress()
//                    callback.invoke(false, exception)
//                }
//            })
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun startDualPoiDocumentScanning(
//        context: Context,
//        appToken: String,
//        userToken: String,
//        email: String,
//        socialMediaEmail: String,
//        phoneNumber: String,
//        flowId: String,
//        bindingView: ViewGroup,
//        callback: (status: Boolean, message: String) -> Unit
//    ) {
//        sid = getSid()
//
//        CoroutineScope(Dispatchers.Main).launch {
//            ProgressManager.showProgress(context)
//
//            val ip = withContext(Dispatchers.IO) {
//                getPublicIpAddress()
//            } ?: ""
//
//            Log.e("call", "#### Dual POI IP fetched: $ip")
//
//            // Scan Passport (Front Only) first
//            scanPassport(
//                context,
//                ip,
//                appToken,
//                userToken,
//                email,
//                socialMediaEmail,
//                phoneNumber,
//                flowId,
//                bindingView,
//                callback
//            )
//        }
//    }
//
//    //** Scan Passport
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun scanPassport(
//        context: Context,
//        ip: String,
//        appToken: String,
//        userToken: String,
//        email: String,
//        socialMediaEmail: String,
//        phoneNumber: String,
//        flowId: String,
//        bindingView: ViewGroup,
//        callback: (status: Boolean, message: String) -> Unit
//    ) {
//        // Passport requires Front page scanning only (multipage processing disabled)
//        com.regula.documentreader.api.DocumentReader.Instance()
//            .processParams().multipageProcessing = false
//
//        DocumentReaderData.showScanner(context) { status, message ->
//            // Restore multipageProcessing to true to ensure single POI behaves exactly as before
//            com.regula.documentreader.api.DocumentReader.Instance()
//                .processParams().multipageProcessing = true
//
//            if (status) {
//                passportRawResult = message
//                Log.d(
//                    "DualPOI",
//                    "Passport scan result stored successfully. Length: ${passportRawResult?.length}"
//                )
//
//                // Immediately start scanning ID Document (Front + Back)
//                scanIdDocument(
//                    context,
//                    ip,
//                    appToken,
//                    userToken,
//                    email,
//                    socialMediaEmail,
//                    phoneNumber,
//                    flowId,
//                    bindingView,
//                    callback
//                )
//            } else {
//                ProgressManager.dismissProgress()
//                callback.invoke(false, message)
//            }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun scanIdDocument(
//        context: Context,
//        ip: String,
//        appToken: String,
//        userToken: String,
//        email: String,
//        socialMediaEmail: String,
//        phoneNumber: String,
//        flowId: String,
//        bindingView: ViewGroup,
//        callback: (status: Boolean, message: String) -> Unit
//    ) {
//        // ID Document requires Front and Back scanning (multipage processing enabled)
//        com.regula.documentreader.api.DocumentReader.Instance()
//            .processParams().multipageProcessing = true
//
//        DocumentReaderData.showScanner(context) { status, message ->
//            if (status) {
//                idDocumentRawResult = message
//                Log.d(
//                    "DualPOI",
//                    "ID Document scan result stored successfully. Length: ${idDocumentRawResult?.length}"
//                )
//
//                // Store in rawResult to feed into the existing verification/upload flow
//                rawResult = message
//
//                // Continue verification with liveness and data upload
//                continueExistingFlowAfterScanning(
//                    context,
//                    ip,
//                    appToken,
//                    userToken,
//                    email,
//                    socialMediaEmail,
//                    phoneNumber,
//                    flowId,
//                    bindingView,
//                    callback
//                )
//            } else {
//                ProgressManager.dismissProgress()
//                callback.invoke(false, message)
//            }
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun continueExistingFlowAfterScanning(
//        context: Context,
//        ip: String,
//        appToken: String,
//        userToken: String,
//        email: String,
//        socialMediaEmail: String,
//        phoneNumber: String,
//        flowId: String,
//        bindingView: ViewGroup,
//        callback: (status: Boolean, message: String) -> Unit
//    ) {
//        val source = "Android v2.22"
//
//        if (flowId == "10015" || flowId == "10016") {
//            initiateDualPoiUpdateData(
//                context = context,
//                token = appToken,
//                sessionId = "0",
//                email = email,
//                flowId = flowId,
//                source = source,
//                callback = callback
//            )
//            //ProgressManager.dismissProgress()
//            //Log.d("DualPOI", "Dual POI scanning completed successfully. Skipping upload API call.")
//            //callback.invoke(true, "Dual POI scanning completed successfully.")
//        } else {
//            // Trigger Dual POI specific liveness request flow
//            faceSessionCreateRequestDualPoi(
//                context,
//                email,
//                userToken,
//                appToken,
//                socialMediaEmail,
//                phoneNumber,
//                ip,
//                flowId,
//                source,
//                bindingView,
//                callback
//            )
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun faceSessionCreateRequestDualPoi(
//        context: Context,
//        email: String,
//        userToken: String,
//        appToken: String,
//        socialMediaEmail: String,
//        phoneNumber: String,
//        ipadd: String,
//        flowId: String,
//        source: String,
//        bindingView: ViewGroup,
//        callback: (Boolean, String) -> Unit
//    ) {
//        if (!InternetConnectionService.networkAvailable(context)) {
//            return
//        }
//
//        val request = SessionCreateRequestNew()
//        request.email = email
//        request.authToken = userToken
//
//        SessionCreateDataNew.createSession(
//            context,
//            appToken,
//            request,
//            object : ResultListener<SessionCreateResponseNew> {
//                override fun onSuccess(response: SessionCreateResponseNew?) {
//                    val sessionId = response?.sessionId!!
//
//                    showDualPoiFaceScanner(
//                        context = context,
//                        email = email,
//                        userToken = userToken,
//                        appToken = appToken,
//                        socialMediaEmail = socialMediaEmail,
//                        phoneNumber = phoneNumber,
//                        ipadd = ipadd,
//                        flowId = flowId,
//                        source = source,
//                        sessionId = sessionId,
//                        bindingView = bindingView,
//                        callback = callback
//                    )
//                }
//
//                override fun onError(exception: String) {
//                    ProgressManager.dismissProgress()
//                    callback.invoke(false, exception)
//                }
//            })
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun initiateDualPoiUpdateData(
//        context: Context,
//        token: String,
//        sessionId: String,
//        email: String,
//        flowId: String,
//        source: String,
//        callback: (Boolean, String) -> Unit
//    ) {
//
//        val request = DualPoiInitiateRequest()
//
//        request.sessionId = sessionId
//        request.workflow = flowId
//        request.source = source
//        request.email = email
//
//        // Passport JSON
//        val passportJson = JsonParser.parseString(passportRawResult ?: "{}").asJsonObject
//        request.passportData = passportJson
//
//        // ID Document JSON
//        val idDocumentJson = JsonParser.parseString(idDocumentRawResult ?: "{}").asJsonObject
//        request.idDocumentData = idDocumentJson
//
//        // Liveness JSON
//        val livenessJson = JsonObject().apply {
//            addProperty("status", "PASS")
//        }
//        request.livenessData = livenessJson
//
//        DualPoiInitiate.initiateDualPoiUpdatedata(
//            context,
//            token,
//            request,
//            object : ResultListener<DualPoiInitiateResponse> {
//
//                override fun onSuccess(response: DualPoiInitiateResponse?) {
//                    ProgressManager.dismissProgress()
//                    dualPoiSessionId= response!!.sessionId!!
//                    Log.d("DualPOI", response?.message ?: "Dual POI initiated successfully")
//                    callback.invoke(
//                        true,
//                        response?.message ?: "Dual POI initiated successfully"
//                    )
//                }
//
//                override fun onError(exception: String) {
//                    ProgressManager.dismissProgress()
//                    Log.d("DualPOI", exception?.toString() ?: "Dual POI Unsuccessfully")
//                    callback.invoke(false, exception)
//                }
//            }
//        )
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun showDualPoiFaceScanner(
//        context: Context,
//        email: String,
//        userToken: String,
//        appToken: String,
//        socialMediaEmail: String,
//        phoneNumber: String,
//        ipadd: String,
//        flowId: String,
//        source: String,
//        sessionId: String,
//        bindingView: ViewGroup,
//        callback: (Boolean, String) -> Unit
//    ) {
//        ProgressManager.dismissProgress()
//        initFaceDetector(context, sessionId, bindingView) {
//            ProgressManager.showProgress(context)
//
//            initiateDualPoiUpdateData(
//                context = context,
//                token = appToken,
//                sessionId = sessionId,
//                email = email,
//                flowId = flowId,
//                source = source,
//                callback = callback
//            )
//            // Liveness check has finished. Skip the upload API call for Dual POI flow.
//            //ProgressManager.dismissProgress()
////            Log.d(
////                "DualPOI",
////                "Dual POI Face Liveness completed successfully. Skipping upload API call."
////            )
////            callback.invoke(
////                true,
////                "Dual POI scanning and liveness verification completed successfully."
////            )
//        }
//    }


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
                    val source = "Android v2.23"

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

        // Phone IP address and Device Type
        val deviceType = DeviceInfoUtil.getDeviceType()
        val ipAddress = DeviceIpUtil.getLocalIpAddress()

        val getDeviceLanguage = Locale.getDefault().language

        val uploaddataRequest = UploadDataRequest()
        uploaddataRequest.ipAddress = ipAddress
        uploaddataRequest.deviceType = deviceType
        uploaddataRequest.email = userEmail
        uploaddataRequest.randomid = sid
        uploaddataRequest.socialMediaEmail = socialMediaEmail
        uploaddataRequest.phoneNumber = phoneNumber
        uploaddataRequest.ipadd = ipadd
        uploaddataRequest.workflow = flowId
        uploaddataRequest.source = source
        uploaddataRequest.language = getDeviceLanguage
        uploaddataRequest.sessionId = sessionId
        val rawData = JsonParser.parseString(rawResult).asJsonObject
        uploaddataRequest.idvData = rawData

        InitiateData.uploadData(
            context,
            token,
            uploaddataRequest,
            object : ResultListener<UploadDataResponse> {
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
        completion: ResultListener<TransactionDetailResponse>
    ) {
        if (!InternetConnectionService.networkAvailable(context)) {
            completion.onError(context.getString(R.string.internet_connection_not_found))
            return
        }
        if (appToken.isNullOrEmpty()) {
            completion.onError(context.getString(R.string.app_token_is_required))
            return
        }


        Log.e("######","single Poi "+sid)

        ProgressManager.showProgress(context)
        TransactionDetail.transactionDetails(
            context,
            appToken,
            sid,
            object : ResultListener<TransactionDetailResponse> {
                override fun onSuccess(response: TransactionDetailResponse?) {
                    ProgressManager.dismissProgress()
                    completion.onSuccess(response)
                }

                override fun onError(exception: String) {
                    ProgressManager.dismissProgress()
                    completion.onError(exception)
                }
            })
        Log.e("######","single Poi "+sid)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getDualPoiData(
//        context: Context,
//        appToken: String,
//        completion: ResultListener<DualPoiTransactionResponse>
//    ) {
//        if (!InternetConnectionService.networkAvailable(context)) {
//            completion.onError(context.getString(R.string.internet_connection_not_found))
//            return
//        }
//        if (appToken.isNullOrEmpty()) {
//            completion.onError(context.getString(R.string.app_token_is_required))
//            return
//        }
//
//        Log.e("######","Double Poi "+sid)
//        ProgressManager.showProgress(context)
//        DualPoiTransactionDetail.dualPoiTransactionDetails(
//            context,
//            appToken,
//            dualPoiSessionId,
//            object : ResultListener<DualPoiTransactionResponse> {
//                override fun onSuccess(response: DualPoiTransactionResponse?) {
//                    ProgressManager.dismissProgress()
//                    completion.onSuccess(response)
//                }
//
//                override fun onError(exception: String) {
//                    ProgressManager.dismissProgress()
//                    completion.onError(exception)
//                }
//            })
//        Log.e("######","Double Poi "+sid)
//    }


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

    private fun showVpnBlockedPopup(context: Context) {

        val activity = context as? android.app.Activity ?: return

        activity.runOnUiThread {

            androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle("VPN Detected")
                .setMessage(
                    "VPN usage is not allowed. Please disable VPN to continue using the application."
                )
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

}
