package com.mevron.rides.driver.documentcheck.data.network

import com.mevron.rides.driver.documentcheck.data.model.GetDocumentResponse
import retrofit2.Response
import retrofit2.http.GET

interface DocumentAPI {
    @GET("api/v1/driver/auth/document-status")
    suspend fun getDocumentStatus(): Response<GetDocumentResponse>

}