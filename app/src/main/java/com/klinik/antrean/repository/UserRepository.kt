package com.klinik.antrean.repository

import com.klinik.antrean.api.UserApi
import com.klinik.antrean.request.CreateRequest
import com.klinik.antrean.request.UserRequest
import com.klinik.antrean.response.ReadResponse
import com.klinik.antrean.response.UserResponse
import com.klinik.antrean.ui.model.MetaData
import retrofit2.Response
/**
 * Created by MuhamadRiyadi on 28/04/2023
 * Phone: 08174100212
 * IG: @_riyadimoch
 */
class UserRepository {
    suspend fun loginUser(userRequest: UserRequest): Response<UserResponse>? {
        return  UserApi.getApi()?.loginUser(username = userRequest.username, password = userRequest.password)
    }

    suspend fun readUser(token: String): Response<ReadResponse>? {
        return  UserApi.getApi()?.readUser(token = token)
    }

    suspend fun createUser(createRequest: CreateRequest): Response<MetaData>? {
        return  UserApi.getApi()?.createUser(createRequest)
    }
}