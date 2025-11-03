package com.sdk.ipassplussdk.apis

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.sdk.ipassplussdk.core.iPassSDKManger
import com.sdk.ipassplussdk.utils.Constants
import com.sdk.ipassplussdk.utils.ServerUrls
import com.sdk.ipassplussdk.utils.SharedPrefUtil

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// ApiClient
@RequiresApi(Build.VERSION_CODES.O)
fun ApiClient(context: Context, x_api_key: String): Retrofit? {

    var baseurl = ""
    try {
        baseurl = SharedPrefUtil(context).getString(Constants.BASE_URL)!!
    } catch (e: Exception) {
        Log.e("Client", "Base url not found")
    }



    if (baseurl.isNullOrEmpty()) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder().baseUrl(ServerUrls.base_url)
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create()).client(
                OkHttpClient.Builder()
                    .connectTimeout(200, TimeUnit.SECONDS)
                    .writeTimeout(200, TimeUnit.SECONDS)
                    .readTimeout(200, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor { chain ->
                        val original: Request = chain.request()
                        val requestBuilder = build(original).addHeader("Authorization",x_api_key)// Header Request Builder
                        chain.proceed(requestBuilder.build())}
                    .build()
            ).build()
    } else {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder().baseUrl(baseurl)
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create()).client(
                OkHttpClient.Builder()
                    .connectTimeout(200, TimeUnit.SECONDS)
                    .writeTimeout(200, TimeUnit.SECONDS)
                    .readTimeout(200, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor { chain ->
                        val original: Request = chain.request()
                        val requestBuilder = build(original).addHeader("Authorization",x_api_key)// Header Request Builder
                        chain.proceed(requestBuilder.build())}
                    .build()
            ).build()
    }

}
