package com.klinik.antrean.response

import okhttp3.ResponseBody
/**
 * Created by MuhamadRiyadi on 28/04/2023
 * Phone: 08174100212
 * IG: @_riyadimoch
 */
sealed class BaseResponse<out T> {
    data class Success<out T>(val data: T? = null) : BaseResponse<T>()
    data class Denied(val data: ResponseBody? = null) : BaseResponse<Nothing>()
    data class Loading(val nothing: Nothing?=null) : BaseResponse<Nothing>()
    data class Error(val msg: String?) : BaseResponse<Nothing>()
    data class Notified(val msg: String?) : BaseResponse<Nothing>()
}