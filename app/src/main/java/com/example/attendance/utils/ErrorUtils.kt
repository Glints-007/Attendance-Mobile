package com.example.attendance.utils

import com.example.attendance.api.APIClient
import com.example.attendance.api.APIError
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

object ErrorUtils {
    fun parseError(response: Response<*>): APIError {
        val converter: Converter<ResponseBody, APIError> = APIClient.retrofit()
            .responseBodyConverter(APIError::class.java, arrayOfNulls<Annotation>(0))
        val error:APIError
        try
        {
            error = converter.convert(response.errorBody())!!
        }
        catch (e: IOException) {
            return APIError()
        }
        return error
    }
}