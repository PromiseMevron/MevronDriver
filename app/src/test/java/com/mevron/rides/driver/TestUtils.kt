package com.mevron.rides.driver

import com.mevron.rides.driver.data.model.DefaultResponse
import com.mevron.rides.driver.data.model.SuccessData
import io.mockk.every
import io.mockk.mockk
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

fun <T> mockError(): Response<T> {
    val body: ResponseBody = mockk()
    val mediaType: MediaType = mockk()
    every { body.contentType() }.returns(mediaType)
    every { body.contentLength() }.returns(2)

    return Response.error(401, body)
}

fun mockDefaultResponse(): Response<DefaultResponse> {
    val body = DefaultResponse(
        success = SuccessData(message = "test", status = "SUCCESS")
    )
    val response: Response<DefaultResponse> = mockk {
        every { body() }.returns(body)
        every { isSuccessful }.returns(true)
    }

    return response
}