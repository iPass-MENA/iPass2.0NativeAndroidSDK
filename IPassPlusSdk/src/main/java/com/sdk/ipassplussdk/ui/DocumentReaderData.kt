package com.sdk.ipassplussdk.ui

import android.annotation.SuppressLint
import android.content.Context
import android.nfc.NfcManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.regula.documentreader.api.DocumentReader
import com.regula.documentreader.api.completions.IDocumentReaderCompletion
import com.regula.documentreader.api.completions.rfid.IRfidReaderCompletion
import com.regula.documentreader.api.config.ScannerConfig
import com.regula.documentreader.api.enums.DocReaderAction
import com.regula.documentreader.api.enums.Scenario
import com.regula.documentreader.api.enums.eRFID_DataFile_Type
import com.regula.documentreader.api.enums.eRFID_NotificationCodes
import com.regula.documentreader.api.errors.DocReaderRfidException
import com.regula.documentreader.api.errors.DocumentReaderException
import com.regula.documentreader.api.results.DocumentReaderNotification
import com.regula.documentreader.api.results.DocumentReaderResults
import com.sdk.ipassplussdk.R


@SuppressLint("StaticFieldLeak")
object DocumentReaderData {
    private var context: Context? = null
    private var rawResult: String? = null
    private var callback: (Boolean, String) -> Unit = { _, _ ->}

    @RequiresApi(Build.VERSION_CODES.O)
    fun showScanner(context: Context,
                    callback: (Boolean, String) -> Unit
                    ) {
        this.context = context
        this.callback = callback

        val scannerConfig = ScannerConfig.Builder(Scenario.SCENARIO_FULL_AUTH).build()
        DocumentReader.Instance().showScanner(context, scannerConfig, completion)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val completion = IDocumentReaderCompletion { action, results, error ->
        DocumentReader.Instance().customization().edit().setUiCustomizationLayer(null).apply()
        if (action == DocReaderAction.COMPLETE
            || action == DocReaderAction.TIMEOUT) {

            var supportsRfid = false

            val manager = context!!.getSystemService(Context.NFC_SERVICE) as NfcManager
            val adapter = manager.defaultAdapter
            if (adapter != null && adapter.isEnabled) {
                //Yes NFC available
                supportsRfid = true
            } else if (adapter != null && !adapter.isEnabled) {
                //NFC is not enabled.Need to enable by the user.
                supportsRfid = false
            } else {
                //NFC is not supported
                supportsRfid = false
            }

            if (results?.textResult?.availableSourceList?.size != 0) {

                if (results?.chipPage != 0 && supportsRfid) {
                    DocumentReader.Instance()
                        .startRFIDReader(context!!, object : IRfidReaderCompletion() {
                            override fun onChipDetected() {
                            }

                            override fun onProgress(notification: DocumentReaderNotification) {
                                rfidProgress(notification.code, notification.value)
                            }

                            override fun onRetryReadChip(exception: DocReaderRfidException) {
                            }

                            override fun onCompleted(
                                rfidAction: Int,
                                results_RFIDReader: DocumentReaderResults?,
                                error: DocumentReaderException?
                            ) {
                                if (rfidAction == DocReaderAction.COMPLETE) {
                                    DocumentReader.Instance().processParams().debugSaveLogs = false
                                    DocumentReader.Instance()
                                        .processParams().debugSaveCroppedImages = false
                                    DocumentReader.Instance().processParams().debugSaveRFIDSession =
                                        false
                                    rawResult = results_RFIDReader?.rawResult!!
                                    callback.invoke(true, rawResult!!)
                                } else if (rfidAction == DocReaderAction.CANCEL) {
                                    rawResult = results?.rawResult
                                    callback.invoke(true, rawResult!!)
                                } else if (rfidAction == DocReaderAction.TIMEOUT) {
                                    Toast.makeText(
                                        context,
                                        context!!.getString(R.string.something_went_wrong_with_nfc),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    rawResult = results?.rawResult
                                    callback.invoke(true, rawResult!!)
                                } else if (rfidAction == DocReaderAction.ERROR) {
                                    Toast.makeText(
                                        context,
                                        context!!.getString(R.string.something_went_wrong_with_nfc),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    rawResult = results?.rawResult
                                    callback.invoke(true, rawResult!!)
                                } else {
                                    rawResult = results?.rawResult
                                    callback.invoke(true, rawResult!!)
                                }
                            }
                        })
                } else {
                    rawResult = results?.rawResult
                    callback.invoke(true, rawResult!!)
                }
            } else {
                callback.invoke(false, "Text results empty")
            }

        }
        else if (action == DocReaderAction.CANCEL) {
                if (DocumentReader.Instance().functionality().isManualMultipageMode)
                    DocumentReader.Instance().functionality().edit().setManualMultipageMode(false).apply()
                callback.invoke(false, context!!.getString(R.string.scanning_cancelled))
            }
            else if (action == DocReaderAction.ERROR) {
                callback.invoke(false, error?.message!!)
            }
    }

    @SuppressLint("WrongConstant")
    fun rfidProgress(code: Int, value: Int) {
        val hiword = code and -0x10000
        val loword = code and 0x0000FFFF
        when (hiword) {
            eRFID_NotificationCodes.RFID_NOTIFICATION_PCSC_READING_DATAGROUP -> if (value == 0) {
//                Log.d("Rfid", "Current group: " + String.format(
//                    context?.getString(com.regula.documentreader.api.R.string.strReadingRFIDDG)!!,
//                    eRFID_DataFile_Type.getTranslation(
//                        context, loword
//                    )))
            }
        }
    }


}