package com.sdk.ipassplussdk.core

import com.regula.documentreader.api.DocumentReader
import com.regula.documentreader.api.params.AuthenticityParams
import com.regula.documentreader.api.params.LivenessParams

object configProperties {

    fun needHologramDetection(value:Boolean){
        val authenticityParams = AuthenticityParams.defaultParams()

        authenticityParams.useLivenessCheck = value

        authenticityParams.livenessParams = LivenessParams.defaultParams()
        authenticityParams.livenessParams?.checkHolo = value
        authenticityParams.livenessParams?.checkOVI = value
        authenticityParams.livenessParams?.checkED = value
        authenticityParams.livenessParams?.checkMLI = value

        authenticityParams.checkImagePatterns = value
        authenticityParams.checkPhotoEmbedding = value
        authenticityParams.checkBarcodeFormat = value
        authenticityParams.checkPhotoComparison = value
        authenticityParams.checkUVLuminiscence = value
        authenticityParams.checkFibers = value
        authenticityParams.checkExtMRZ = value
        authenticityParams.checkExtOCR = value
        authenticityParams.checkIRB900 = value
        authenticityParams.checkIRVisibility = value
        authenticityParams.checkIPI = value
        authenticityParams.checkAxial = value
        authenticityParams.checkLetterScreen = value

        DocumentReader.Instance().processParams().useAuthenticityCheck = true
        DocumentReader.Instance().processParams().authenticityParams = authenticityParams

        DocumentReader.Instance().processParams().respectImageQuality = true
        DocumentReader.Instance().processParams().imageQA.focusCheck = true
        DocumentReader.Instance().processParams().imageQA.glaresCheck = true
        DocumentReader.Instance().processParams().imageQA.focusCheck = true
        DocumentReader.Instance().processParams().imageQA.colornessCheck = true

//        Log.e("####", "Hologram $value")
//        DocumentReader.Instance().processParams().checkHologram = value
    }

}