package com.klinik.antrean.api

import com.klinik.antrean.ApiClient
import com.klinik.antrean.request.CreateRequest
import com.klinik.antrean.response.ReadResponse
import com.klinik.antrean.response.UserResponse
import com.klinik.antrean.ui.model.MetaData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by MuhamadRiyadi on 28/04/2023
 * Phone: 08174100212
 * IG: @_riyadimoch
 */
interface UserApi {
    @GET("auth")
    suspend fun loginUser(
        @Header("x-username") username: String,
        @Header("x-password") password: String
    ): Response<UserResponse>

    @GET("auth/read")
    suspend fun readUser(
        @Header("x-token") token: String
    ): Response<ReadResponse>

    @POST("auth/create")
    suspend fun createUser(
        @Body createRequest: CreateRequest?
    ): Response<MetaData>

    companion object {
        fun getApi(): UserApi? {
            return ApiClient.client?.create(UserApi::class.java)
        }
    }
}