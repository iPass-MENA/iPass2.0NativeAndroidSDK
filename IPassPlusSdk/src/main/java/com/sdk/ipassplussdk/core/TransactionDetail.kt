package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.sdk.ipassplussdk.R
import com.sdk.ipassplussdk.apis.ApiClient
import com.sdk.ipassplussdk.apis.ApiInterface
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.model.response.liveness_facesimilarity.FaceScannerResponse
import com.sdk.ipassplussdk.model.response.transaction_details.TransactionDetailResponse
import com.sdk.ipassplussdk.utils.Constants
import com.sdk.ipassplussdk.utils.ErrorHandler
import com.sdk.ipassplussdk.utils.InternetConnectionService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object TransactionDetail {
    @RequiresApi(Build.VERSION_CODES.O)
    fun transactionDetails(
        context: Context,
        token: String,
        sessionId: String,
        completion: ResultListener<TransactionDetailResponse>
    ) {
        if (InternetConnectionService.networkAvailable(context)) {
            ApiClient(context, "")?.create(ApiInterface::class.java)!!
                .transactionDetails(token, sessionId).enqueue(object :
                    Callback<TransactionDetailResponse> {
                    override fun onResponse(
                        call: Call<TransactionDetailResponse>,
                        response: Response<TransactionDetailResponse>
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

                    override fun onFailure(call: Call<TransactionDetailResponse>, t: Throwable) {
                        completion.onError(t.message.toString())
                    }
                })
        } else {
            completion.onError(context.getString(R.string.internet_connection_not_found))
        }
    }
}