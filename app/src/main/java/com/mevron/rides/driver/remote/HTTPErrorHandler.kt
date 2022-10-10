package com.mevron.rides.driver.remote

import com.google.gson.Gson
import com.mevron.rides.driver.remote.error.ErrorModel
import com.mevron.rides.driver.util.Constants
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

object HTTPErrorHandler {

    fun <T: Any?> handleErrorWithCode(response: Response<T>): ErrorModel?{
        return try{

            if(!response.isSuccessful){
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorModel::class.java)
                error
            }else null
        }catch (ex: Exception){
            ex.printStackTrace()
            ErrorModel(error = com.mevron.rides.driver.remote.error.Error(Constants.UNEXPECTED_ERROR, "Failed"))

        }
    }

    fun httpFailWithCode(t: Exception): ErrorModel{
        return if(t is SocketTimeoutException || t is HttpException)
            ErrorModel(error = com.mevron.rides.driver.remote.error.Error("Network Error", "Failed"))
        else
            ErrorModel(error = com.mevron.rides.driver.remote.error.Error("Unknown Error", "Failed"))
    }
}