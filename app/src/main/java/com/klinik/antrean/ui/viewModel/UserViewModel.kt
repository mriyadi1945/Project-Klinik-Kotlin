package com.klinik.antrean.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.klinik.antrean.repository.UserRepository
import com.klinik.antrean.request.CreateRequest
import com.klinik.antrean.request.UserRequest
import com.klinik.antrean.response.BaseResponse
import com.klinik.antrean.response.ReadResponse
import com.klinik.antrean.response.UserResponse
import com.klinik.antrean.ui.model.MetaData
import kotlinx.coroutines.launch

/**
 * Created by MuhamadRiyadi on 28/04/2023
 * Phone: 08174100212
 * IG: @_riyadimoch
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<UserResponse>> = MutableLiveData()
    val readResult: MutableLiveData<BaseResponse<ReadResponse>> = MutableLiveData()
    val createResult: MutableLiveData<BaseResponse<MetaData>> = MutableLiveData()

    fun loginUser(username: String, password: String) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                if (username.isNotEmpty() && password.isNotEmpty())
                {
                    val userRequest = UserRequest(
                        username = username,
                        password = password
                    )

                    val response = userRepo.loginUser(userRequest = userRequest)
                    if (response?.code() == 200) {
                        loginResult.value = BaseResponse.Success(response.body())
                    }
                    else if (response?.code() == 401 || response?.code() == 403 || response?.code() == 404 || response?.code() == 422) {
                        loginResult.value = BaseResponse.Denied(response.errorBody())
                    }
                    else {
                        loginResult.value = BaseResponse.Error(response?.message())
                    }
                }
                else
                {
                    loginResult.value = BaseResponse.Notified("Email and Password Required")
                }

            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun readUser(token: String) {
        readResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                if (token.isNotEmpty())
                {
                    val response = userRepo.readUser(token = token)
                    if (response?.code() == 200) {
                        readResult.value = BaseResponse.Success(response.body())
                    }
                    else if (response?.code() == 401 || response?.code() == 403 || response?.code() == 404 || response?.code() == 422) {
                        readResult.value = BaseResponse.Denied(response.errorBody())
                    }
                    else {
                        readResult.value = BaseResponse.Error(response?.message())
                    }
                }
                else
                {
                    readResult.value = BaseResponse.Notified("Email and Password Required")
                }

            } catch (ex: Exception) {
                readResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun createUser(
        username: String,
        password: String,
        name: String,
        akses: String,
        klinik: String,
        cabang: String,
        email: String
    ) {
        createResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                if (username.isNotEmpty()
                    && password.isNotEmpty()
                    && name.isNotEmpty()
                    && akses.isNotEmpty()
                    && klinik.isNotEmpty()
                    && cabang.isNotEmpty()
                    && email.isNotEmpty()
                ){
                    val createRequest = CreateRequest(
                        username = username,
                        password = password,
                        nama = name,
                        hakakses = akses,
                        kdklinik = klinik,
                        kdcabang = cabang,
                        email = email
                    )
                    val response = userRepo.createUser(createRequest)
                    if (response?.code() == 200) {
                        createResult.value = BaseResponse.Success(response.body())
                    }
                    else if (response?.code() == 401 || response?.code() == 403 || response?.code() == 404 || response?.code() == 422) {
                        createResult.value = BaseResponse.Denied(response.errorBody())
                    }
                    else {
                        createResult.value = BaseResponse.Error(response?.message())
                    }
                }
                else
                {
                    createResult.value = BaseResponse.Notified("Username and Password Required")
                }

            } catch (ex: Exception) {
                createResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}