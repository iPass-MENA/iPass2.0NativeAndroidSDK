package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.sdk.ipassplussdk.apis.ApiClient
import com.sdk.ipassplussdk.apis.ApiInterface
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.model.request.DualPoiInitiateRequest
import com.sdk.ipassplussdk.model.response.DualPoiInitiateResponse
import com.sdk.ipassplussdk.utils.InternetConnectionService
import com.sdk.ipassplussdk.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DualPoiInitiate {

    @RequiresApi(Build.VERSION_CODES.O)
    fun initiateDualPoiUpdatedata(
        context: Context,
        token: String,
        request: DualPoiInitiateRequest,
        completion: ResultListener<DualPoiInitiateResponse>
    ) {

        if (InternetConnectionService.networkAvailable(context)) {

            ApiClient(context, "")
                ?.create(ApiInterface::class.java)!!
                .initiateDualPoi(token, request)
                .enqueue(object : Callback<DualPoiInitiateResponse> {

                    override fun onResponse(
                        call: Call<DualPoiInitiateResponse>,
                        response: Response<DualPoiInitiateResponse>
                    ) {

                        if (response.isSuccessful) {
                            completion.onSuccess(response.body())
                        } else {
                            try {
                                completion.onError(response.errorBody()?.string().orEmpty())
                            } catch (e: Exception) {
                                completion.onError(
                                    context.getString(R.string.data_processing_error)
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<DualPoiInitiateResponse>,
                        t: Throwable
                    ) {
                        completion.onError(t.message ?: "")
                    }
                })

        } else {
            completion.onError(
                context.getString(R.string.internet_connection_not_found)
            )
        }
    }
}