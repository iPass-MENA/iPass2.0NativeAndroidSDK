package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.sdk.ipassplussdk.R
import com.sdk.ipassplussdk.apis.ApiClient
import com.sdk.ipassplussdk.apis.ApiInterface
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.model.response.transaction_details.DualPoiTransactionResponse

import com.sdk.ipassplussdk.utils.InternetConnectionService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DualPoiTransactionDetail {

    @RequiresApi(Build.VERSION_CODES.O)
    fun dualPoiTransactionDetails(
        context: Context,
        token: String,
        sessionId: String,
        completion: ResultListener<DualPoiTransactionResponse>
    ) {
        if (InternetConnectionService.networkAvailable(context)) {
            ApiClient(context, "")?.create(ApiInterface::class.java)!!
                .dualPoiTransactionDetails(token, sessionId).enqueue(object :
                    Callback<DualPoiTransactionResponse> {
                    override fun onResponse(
                        call: Call<DualPoiTransactionResponse>,
                        response: Response<DualPoiTransactionResponse>
                    ) {
                        print("Response ==> $response")
                        if (response.isSuccessful) {
                            completion.onSuccess(response.body()!!)
                        } else {
                            try {
                                completion.onError(response.errorBody()?.string().toString())
                            }catch (e: Exception){
                                completion.onError(context.getString(R.string.something_went_wrong))
                            }
                        }
                    }

                    override fun onFailure(call: Call<DualPoiTransactionResponse>, t: Throwable) {
                        completion.onError(t.message.toString())
                    }
                })
        } else {
            completion.onError(context.getString(R.string.internet_connection_not_found))
        }
    }
}
