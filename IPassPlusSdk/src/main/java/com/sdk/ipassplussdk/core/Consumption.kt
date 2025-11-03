package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.sdk.ipassplussdk.R
import com.sdk.ipassplussdk.apis.ApiClient
import com.sdk.ipassplussdk.apis.ApiInterface
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.model.response.authentication.AuthenticationResponse
import com.sdk.ipassplussdk.model.response.consumption.CustomerAccessResponse
import com.sdk.ipassplussdk.model.response.errorbody.ErrorBodyResponse
import com.sdk.ipassplussdk.utils.Constants
import com.sdk.ipassplussdk.utils.InternetConnectionService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Consumption {


    @RequiresApi(Build.VERSION_CODES.O)
    fun checkAccess(
        context: Context,
        token: String,
        language:String,
        completion: ResultListener<CustomerAccessResponse>
    ) {
        if (InternetConnectionService.networkAvailable(context)) {
            ApiClient(context, "")?.create(ApiInterface::class.java)!!
                .checkAccess(token, language).enqueue(object :
                    Callback<CustomerAccessResponse> {
                    override fun onResponse(
                        call: Call<CustomerAccessResponse>,
                        response: Response<CustomerAccessResponse>
                    ) {
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

                    override fun onFailure(call: Call<CustomerAccessResponse>, t: Throwable) {
                        completion.onError(t.message.toString())
                    }
                })
        } else {
            completion.onError(context.getString(R.string.internet_connection_not_found))
        }

    }
}