package com.sdk.ipassplussdk.apis
import com.sdk.ipassplussdk.model.request.DualPoiInitiateRequest
import com.sdk.ipassplussdk.model.request.authentication.AuthenticationRequest
import com.sdk.ipassplussdk.model.request.create_aws_session.SessionCreateRequestNew
import com.sdk.ipassplussdk.model.request.initiate_data.UploadDataRequest
import com.sdk.ipassplussdk.model.response.DualPoiInitiateResponse
import com.sdk.ipassplussdk.model.response.authentication.AuthenticationResponse
import com.sdk.ipassplussdk.model.response.consumption.CustomerAccessResponse
import com.sdk.ipassplussdk.model.response.create_aws_session.SessionCreateResponseNew
import com.sdk.ipassplussdk.model.response.initiate_data.UploadDataResponse
import com.sdk.ipassplussdk.model.response.transaction_details.DualPoiTransactionResponse
import com.sdk.ipassplussdk.model.response.transaction_details.TransactionDetailResponse
import com.sdk.ipassplussdk.utils.ServerUrls
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interface for endpoint methods
 */
interface ApiInterface {

    @POST(ServerUrls.url_authentication)
    fun auth(
        @Body authRequest: AuthenticationRequest
    ): Call<AuthenticationResponse>

    @GET(ServerUrls.url_customer_access)
    fun checkAccess(
        @Query("token") token : String,
        @Query("language") language : String
    ): Call<CustomerAccessResponse>

    @POST(ServerUrls.url_aws_session_create)
    fun createSession(
        @Query("token") token: String,
        @Body sessionCreateRequest: SessionCreateRequestNew
    ): Call<SessionCreateResponseNew>

    @POST(ServerUrls.url_upload_data)
    @Headers("Content-Type: application/json")
    fun uploadData(
        @Query("token") token: String,
        @Body uploadDataRequest: UploadDataRequest
    ): Call<UploadDataResponse>

    @GET(ServerUrls.url_transaction_detail)
    fun transactionDetails(
        @Query("token") token: String,
        @Query("sesid") transactionid : String,
    ): Call<TransactionDetailResponse>


    @POST(ServerUrls.url_dual_poi_initiate)
    @Headers("Content-Type: application/json")
    fun initiateDualPoi(
        @Query("token") token: String,
        @Body request: DualPoiInitiateRequest
    ): Call<DualPoiInitiateResponse>

    @GET(ServerUrls.url_dual_poi_transaction_detail)
    fun dualPoiTransactionDetails(
        @Query("token") token: String,
        @Query("sesid") transactionid : String,
    ): Call<DualPoiTransactionResponse>

}
