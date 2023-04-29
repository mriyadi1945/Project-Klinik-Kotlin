package com.klinik.antrean.ui.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("hakakses")
    var hakakses: String,
    @SerializedName("kdklinik")
    var kdklinik: String,
    @SerializedName("kdcabang")
    var kdcabang: String,
)

data class Token(
    @SerializedName("token")
    var token: String
)

data class MetaData(
    @SerializedName("message")
    var message: String,
    @SerializedName("code")
    var code: Int
)