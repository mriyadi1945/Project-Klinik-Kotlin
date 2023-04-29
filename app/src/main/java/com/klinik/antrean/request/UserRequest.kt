package com.klinik.antrean.request

import com.google.gson.annotations.SerializedName

/**
 * Created by MuhamadRiyadi on 28/04/2023
 * Phone: 08174100212
 * IG: @_riyadimoch
 */
class UserRequest(
    @SerializedName("x-username")
    var username: String,
    @SerializedName("x-password")
    var password: String
)
class CreateRequest(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("nama")
    var nama: String,
    @SerializedName("hakakses")
    var hakakses: String,
    @SerializedName("kdklinik")
    var kdklinik: String,
    @SerializedName("kdcabang")
    var kdcabang: String,
    @SerializedName("email")
    var email: String
)