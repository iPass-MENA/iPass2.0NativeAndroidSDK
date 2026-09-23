package com.sdk.ipassplussdk.model.response.transaction_details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class DualPoiTransactionResponse(
    @SerialName("Apistatus")
    val Apistatus: Boolean = false,

    @SerialName("Apimessage")
    val Apimessage: String? = null,

    val sessionId: String? = null,

    val status: String? = null,

    val documents: List<Document>? = null,

    val livenessData: List<LivenessData>? = null
)

@Serializable
data class Document(
    @SerialName("_id")
    val id: String? = null,

    val sid: String? = null,

    val source: String? = null,

    @SerialName("doc_type")
    val docType: String? = null,

    val createdAt: String? = null,

    val email: String? = null,

    val workflow: String? = null,

    @SerialName("created_By")
    val createdBy: String? = null,

    val fullName: String? = null,

    val issCont: String? = null,

    val docTabSts: String? = null,

    val livTabSts: String? = null,

    val amlTabSts: String? = null,

    val simSts: String? = null,

    val similarutyStatus: String? = null,

    val overAllTransSts: String? = null,

    val livenessStatus: String? = null,

    val cardType: String? = null,

    val passport_data: PassportData? = null,

    val passportData: PassportDataSummary? = null,

    val id_document_data: IdDocumentData? = null,

    val idCardData: IdCardDataSummary? = null,

    val liveness_data: LivenessStatus? = null,

    @SerialName("__v")
    val version: Int? = null
)

@Serializable
data class PassportData(
    val ChipPage: Int? = null,

    val ProcessingFinished: Int? = null,

    val TransactionInfo: TransactionInfo? = null,

    val elapsedTime: Long? = null,

    val lightType: List<Int>? = null,

    val morePagesAvailable: Int? = null,

    val processCommandRes: Int? = null,

    val resolutionType: Int? = null,

    val fullName: String? = null,

    val cardType: String? = null,

    val issueCountry: String? = null,

    val status: String? = null
)

@Serializable
data class IdDocumentData(
    val ChipPage: Int? = null,

    val ProcessingFinished: Int? = null,

    val TransactionInfo: TransactionInfo? = null,

    val elapsedTime: Long? = null,

    val lightType: List<Int>? = null,

    val morePagesAvailable: Int? = null,

    val processCommandRes: Int? = null,

    val resolutionType: Int? = null,

    val fullName: String? = null,

    val cardType: String? = null,

    val issueCountry: String? = null,

    val status: String? = null
)

@Serializable
data class TransactionInfo(
    val ComputerName: String? = null,

    val DateTime: String? = null,

    val DocumentsDatabase: DocumentsDatabase? = null,

    val SystemInfo: String? = null,

    val Tag: String? = null,

    val TransactionID: String? = null,

    val UserName: String? = null,

    val Version: String? = null
)

@Serializable
data class DocumentsDatabase(
    val Description: String? = null,

    val ExportDate: String? = null,

    val ID: String? = null,

    val Version: String? = null
)

@Serializable
data class PassportDataSummary(
    val fullName: String? = null,

    val cardType: String? = null,

    val status: String? = null,

    val issueCountry: String? = null
)

@Serializable
data class IdCardDataSummary(
    val fullName: String? = null,

    val cardType: String? = null,

    val status: String? = null,

    val issueCountry: String? = null
)

@Serializable
data class LivenessStatus(
    val status: String? = null
)

@Serializable
data class LivenessData(
    @SerialName("_id")
    val id: String? = null,

    val sid: String? = null,

    @SerialName("__v")
    val version: Int? = null,

    val email: String? = null,

    val response: LivenessResponse? = null
)

@Serializable
data class LivenessResponse(
    val SessionId: String? = null,

    val Status: String? = null,

    val Confidence: Double? = null,

    val ReferenceImage: ReferenceImage? = null
)

@Serializable
data class ReferenceImage(
    val Bytes: String? = null
)
