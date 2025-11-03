package com.sdk.ipassplussdk.ui

import android.content.Context
import android.content.res.AssetManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.regula.documentreader.api.DocumentReader
import com.regula.documentreader.api.completions.IDocumentReaderPrepareCompletion
import com.regula.documentreader.api.errors.DocumentReaderException
import com.regula.documentreader.api.internal.utils.Common
import com.regula.documentreader.api.params.DocReaderConfig
import com.sdk.ipassplussdk.R
import com.sdk.ipassplussdk.enums.DatabaseType
import com.sdk.ipassplussdk.resultCallbacks.InitializeDatabaseCompletion
import com.sdk.ipassplussdk.utils.InternetConnectionService
import com.sdk.ipassplussdk.utils.LicenseUtil
import com.sdk.ipassplussdk.utils.SharedPrefUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object InitializeDatabase {

    @Transient
    var isInitializedByBleDevice: Boolean = false

/*    @RequiresApi(Build.VERSION_CODES.O)
    fun InitDatabase(context: Context, dbName: String, completion: InitializeDatabaseCompletion){
        if (InternetConnectionService.networkAvailable(context)) {
            if (LicenseUtil.readFileFromAssets("SdkLicense", "sdk.license", context) == null
                && !isInitializedByBleDevice
            ) completion.onCompleted(false, "License not found")

            if (DocumentReader.Instance().isReady)
                onInitComplete(completion)

            val license = LicenseUtil.readFileFromAssets(
                "SdkLicense",
                "sdk.license",
                context
            )

            val pref = SharedPrefUtil(context)

            if (pref.getDbType().equals(dbName)) {
                if (dbName.equals(DatabaseType.ONLINE)) initOnlineDb(license!!, context, completion) else initCustomDb("database/${dbName}.dat", license!!, context, completion)
            } else {
                DocumentReader.Instance().removeDatabase(context)
                DocumentReader.Instance().deinitializeReader()
                pref.setDbType(dbName)
                if (dbName.equals(DatabaseType.ONLINE)) initOnlineDb(license!!, context, completion) else initCustomDb("database/${dbName}.dat", license!!, context, completion)
            }

//            license?.let {
//                initCustomDb("database/${dbName}.dat", it, context, completion)
//            }
        } else {
            completion.onCompleted(false, context.getString(R.string.internet_connection_not_found))
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    fun initOnlineDb(
        context: Context, completion: InitializeDatabaseCompletion
    ) {

        if (InternetConnectionService.networkAvailable(context)) {
            if (LicenseUtil.readFileFromAssets("SdkLicense", "sdk.license", context) == null
                && !isInitializedByBleDevice
            ) completion.onCompleted(false, "License not found")

            if (DocumentReader.Instance().isReady)
                onInitComplete(completion)

            val license = LicenseUtil.readFileFromAssets(
                "SdkLicense",
                "sdk.license",
                context
            )

            val pref = SharedPrefUtil(context)

            if (!pref.getDbType().equals("")) {
                DocumentReader.Instance().removeDatabase(context)
                DocumentReader.Instance().deinitializeReader()
                pref.setDbType("")
//                Log.e("@@@@@", "dbName")
            }
//            Log.e("@@@@", "dbName")

            DocumentReader.Instance().processParams().debugSaveLogs = false
            DocumentReader.Instance().processParams().debugSaveCroppedImages = false
            DocumentReader.Instance().processParams().debugSaveRFIDSession = false

            DocumentReader.Instance().prepareDatabase(context,"Full_authOther", object :
                IDocumentReaderPrepareCompletion {
                override fun onPrepareProgressChanged(progress: Int) {
                    // getting progress
//                    Log.e("@@@@", progress.toString())
                    completion.onProgressChanged(progress)
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onPrepareCompleted(status: Boolean, error: DocumentReaderException?) {
//                 database was prepared
                    val config = DocReaderConfig(license!!)

                    DocumentReader.Instance().initializeReader(context, config) {
                            success, error_initializeReader ->
                        if (success) {
                            onInitComplete(completion)
                        }
                        else {
                            completion.onCompleted(false, error_initializeReader?.message.toString())
                        }
                    }
                }
            })


        } else {
            completion.onCompleted(false, context.getString(R.string.internet_connection_not_found))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initCustomDb(context: Context, dbName: String, completion: InitializeDatabaseCompletion) {

        if (InternetConnectionService.networkAvailable(context)) {
            if (LicenseUtil.readFileFromAssets("SdkLicense", "sdk.license", context) == null
                && !isInitializedByBleDevice
            ) completion.onCompleted(false, "License not found")

            if (DocumentReader.Instance().isReady)
                onInitComplete(completion)

            val license = LicenseUtil.readFileFromAssets(
                "SdkLicense",
                "sdk.license",
                context
            )

            val pref = SharedPrefUtil(context)

            if (!pref.getDbType().equals(dbName)) {
                DocumentReader.Instance().removeDatabase(context)
                DocumentReader.Instance().deinitializeReader()
                pref.setDbType(dbName)
//                Log.e("@@@@@", dbName)
            }
//            Log.e("@@@@", dbName)

//            val config = DocReaderConfig(license!!, Common.getFileContentFromAsset(context, "database/db_full_auth_other.dat"))
            val config = DocReaderConfig(license!!, Common.getFileContentFromAsset(context, "database/$dbName.dat"))
            config. setLicenseUpdate (true)

            DocumentReader.Instance()
                .initializeReader(context, config) {
                        success, error_initializeReader ->
                    if (success) {
                        onInitComplete(completion)
                    }
                    else {
                        completion.onCompleted(false, error_initializeReader?.message.toString())
                    }
                }

        } else {
            completion.onCompleted(false, context.getString(R.string.internet_connection_not_found))
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun onInitComplete(completion: InitializeDatabaseCompletion) {

//        DocumentReader.Instance().functionality().edit().setCameraSize(3840, 2160).apply()
//        DocumentReader.Instance().processParams().debugSaveLogs = false
//        DocumentReader.Instance().processParams().debugSaveCroppedImages = false
//        DocumentReader.Instance().processParams().debugSaveImages = false

//        val path = DocumentReader.Instance().processParams().sessionLogFolder
//        Log.d("SdkLicense" , "Path: $path")


//        DocumentReader.Instance().setLocalizationCallback { stringId ->
//            if(stringId == "strLookingDocument")
//            // return@setLocalizationCallback SettingsActivity.customString
//                return@setLocalizationCallback ""
//            return@setLocalizationCallback null
//        }


        DocumentReader.Instance().processParams().multipageProcessing = true
        DocumentReader.Instance().processParams().dateFormat = "dd-mm-yyyy"
        DocumentReader.Instance().processParams().returnUncroppedImage = true
        DocumentReader.Instance().functionality().edit().setShowSkipNextPageButton(false).apply()
        DocumentReader.Instance().functionality().edit().setCameraSize(3840, 2160).apply()
        DocumentReader.Instance().processParams().respectImageQuality = true
        DocumentReader.Instance().processParams().imageQA.focusCheck = true
        DocumentReader.Instance().processParams().imageQA.glaresCheck = true
        DocumentReader.Instance().processParams().imageQA.focusCheck = true
        DocumentReader.Instance().processParams().imageQA.colornessCheck = true

//        DocumentReader.Instance().version?.apply {
//
//            Log.e("Version", "api - ${this.api!!}")
//            Log.e("Version", "core - ${this.core!!}")
//            Log.e("Version", "coreMode - ${this.coreMode!!}")
//
//            Log.e("Version", "DB ID - ${this.database?.databaseID!!}")
//            Log.e("Version", "DB Description - ${this.database?.databaseDescription!!}")
//            Log.e("Version", "DB version - ${this.database?.version!!}")
//            Log.e("Version", "DB date - ${this.database?.date!!}")
//            Log.e("Version", "DB countriesNumber - ${this.database?.countriesNumber!!}")
//            Log.e("Version", "DB documentsNumber - ${this.database?.documentsNumber!!}")
////            Log.e("Version", "DB size - ${this.database?.size!!}")
//        }

        completion.onCompleted(true,"Success")

    }

}