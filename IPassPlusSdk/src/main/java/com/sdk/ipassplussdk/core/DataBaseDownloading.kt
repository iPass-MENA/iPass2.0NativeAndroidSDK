package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.sdk.ipassplussdk.R
import com.sdk.ipassplussdk.resultCallbacks.InitializeDatabaseCompletion
import com.sdk.ipassplussdk.ui.FaceScannerData
import com.sdk.ipassplussdk.ui.InitializeDatabase
import com.sdk.ipassplussdk.utils.Constants
import com.sdk.ipassplussdk.utils.InternetConnectionService
import com.sdk.ipassplussdk.utils.ServerUrls
import com.sdk.ipassplussdk.utils.SharedPrefUtil
import com.sdk.ipassplussdk.views.ProgressManager

object DataBaseDownloading {

    @RequiresApi(Build.VERSION_CODES.O)
    fun initializePreProcessedDb(context: Context, dbName: String, completion: InitializeDatabaseCompletion, serverUrl: String = ""){
        if (!InternetConnectionService.networkAvailable(context)) {
            completion.onCompleted(false, context.getString(R.string.internet_connection_not_found))
            return
        }

        ProgressManager.showProgress(context)

        if (!serverUrl.isNullOrEmpty()) {
            SharedPrefUtil(context).putString(Constants.BASE_URL, "${serverUrl}node/api/v1/ipass/")
          //  SharedPrefUtil(context).putString(Constants.BASE_URL, "${serverUrl}api/v1/ipass/")
        } else {
            SharedPrefUtil(context).putString(Constants.BASE_URL, ServerUrls.base_url)
        }

        InitializeDatabase.initCustomDb(context, dbName, object : InitializeDatabaseCompletion {
            override fun onProgressChanged(progress: Int) {
                completion.onProgressChanged(progress)
            }

            override fun onCompleted(status: Boolean, message: String?) {
                if (status) {
//                    Log.e("onCompleted## ",message!!)
                    configureFaceScanner(context, completion)
                } else {
//                    Log.e("onCompleted##1 ",message!!)
                    ProgressManager.dismissProgress()
                    completion.onCompleted(status, message)
                }
            }

        })
    }


    //    initialize database for scanning (needs to be initialized at least once before scanning)
    @RequiresApi(Build.VERSION_CODES.O)
    fun initializeDynamicDb(context: Context, completion: InitializeDatabaseCompletion, serverUrl: String = ""){
        if (!InternetConnectionService.networkAvailable(context)) {
            completion.onCompleted(false, context.getString(R.string.internet_connection_not_found))
            return
        }

        ProgressManager.showProgress(context)

        if (!serverUrl.isNullOrEmpty()) {
            SharedPrefUtil(context).putString(Constants.BASE_URL, "${serverUrl}node/api/v1/ipass/")
          //  SharedPrefUtil(context).putString(Constants.BASE_URL, "${serverUrl}api/v1/ipass/")
        }

        InitializeDatabase.initOnlineDb(context, object : InitializeDatabaseCompletion {
            override fun onProgressChanged(progress: Int) {
                completion.onProgressChanged(progress)
            }

            override fun onCompleted(status: Boolean, message: String?) {
                if (status) {
//                    Log.e("onCompleted## ",message!!)
                    configureFaceScanner(context, completion)
                } else {
//                    Log.e("onCompleted##1 ",message!!)
                    ProgressManager.dismissProgress()
                    completion.onCompleted(status, message)
                }
            }

        })
    }

    //    Face scanner configuration
    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureFaceScanner(
        context: Context,
        completion: InitializeDatabaseCompletion
    ) {
        FaceScannerData.configureFaceScanner(context) {
//            Log.e("configureFaceScanner## ","configureFaceScanner")
            ProgressManager.dismissProgress()
//            Log.e("dismissProgress## ","dismissProgress")
            if (it.equals("FaceScannerConfigured")) {
//                Log.e("FaceScannerConfigured## ","FaceScannerConfigured")
                completion.onCompleted(true,
                    context.getString(R.string.database_initialized_successfully))
            } else {
//                Log.e("FaceScannerNotConfigured## ","FaceScannerNotConfigured $it")
                completion.onCompleted(false, context.getString(R.string.facescannernotconfigured))
            }
        }
    }
}