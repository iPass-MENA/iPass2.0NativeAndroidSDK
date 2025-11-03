package com.sdk.ipassplussdk.core

import java.util.HashMap

object Workflows {

    fun getList(): Array<HashMap<String, String>> {

        val fullProcessing: HashMap<String, String> = hashMapOf(
            Pair("flow", "Full processing"),
            Pair("flowId", "10031"),
            Pair("Description", "This flow includes Document Scanning, Document Authenticity, User Liveness, User Face Matching, AML and social platform")
        )

        val idvLivenessAml: HashMap<String, String> = hashMapOf(
            Pair("flow", "IdVerification+liveness+AML"),
            Pair("flowId", "10032"),
            Pair("Description", "This flow includes Document Scanning, Document Authenticity, User Liveness, User Face Matching and AML")
        )

        val idvAml: HashMap<String, String> = hashMapOf(
            Pair("flow", "Idverification+AML"),
            Pair("flowId", "10015"),
            Pair("Description", "This flow includes Document Scanning, Document Authenticity and AML")
        )

        val idverificationLiveness: HashMap<String, String> = hashMapOf(
            Pair("flow", "idverification+liveness"),
            Pair("flowId", "10011"),
            Pair("Description", "This flow includes Document Scanning, Document Authenticity, User Liveness and User Face Matching")
        )

        val workflowList = arrayOf(
            fullProcessing,
            idvLivenessAml,
            idvAml,
            idverificationLiveness
        )

        return workflowList
    }
}