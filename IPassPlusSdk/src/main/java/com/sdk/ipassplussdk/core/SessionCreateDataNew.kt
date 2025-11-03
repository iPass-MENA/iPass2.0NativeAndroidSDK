package com.sdk.ipassplussdk.core
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.sdk.ipassplussdk.R
import com.sdk.ipassplussdk.apis.ApiClient
import com.sdk.ipassplussdk.apis.ApiInterface
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.model.request.create_aws_session.SessionCreateRequestNew
import com.sdk.ipassplussdk.model.response.create_aws_session.SessionCreateResponseNew
import com.sdk.ipassplussdk.utils.Constants
import com.sdk.ipassplussdk.utils.InternetConnectionService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SessionCreateDataNew {
    @RequiresApi(Build.VERSION_CODES.O)
    fun createSession(
        context: Context,
        token: String,
        request: SessionCreateRequestNew,
        completion: ResultListener<SessionCreateResponseNew>
    ) {
        if (InternetConnectionService.networkAvailable(context)) {
            ApiClient(context, "")?.create(ApiInterface::class.java)!!
                .createSession(token, request).enqueue(object :
                    Callback<SessionCreateResponseNew>{
                    override fun onResponse(
                        call: Call<SessionCreateResponseNew>,
                        response: Response<SessionCreateResponseNew>
                    ) {
                        if (response.isSuccessful) {
                            completion.onSuccess(response.body()!!)
                        } else {
                            try {
                                if (response.message().isNullOrEmpty()) {
                                    if (response.errorBody()?.string() != null && !response.errorBody()?.string().toString().equals("")) {
                                        completion.onError(response.errorBody()?.string().toString())
                                    } else {
                                        completion.onError(response.body()?.error?.user?.title!!)
                                    }
                                } else {
                                    completion.onError(response.message())
                                }
                            }catch (e: Exception){
                                completion.onError(context.getString(R.string.something_went_wrong))
                            }
                        }
                    }

                    override fun onFailure(call: Call<SessionCreateResponseNew>, t: Throwable) {
                        completion.onError(t.message.toString())
                    }
                })
        } else {
            completion.onError(context.getString(R.string.internet_connection_not_found))
        }

    }
}