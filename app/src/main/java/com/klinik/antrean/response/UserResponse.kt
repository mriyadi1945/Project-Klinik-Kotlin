package com.klinik.antrean.response

import com.google.gson.annotations.SerializedName
import com.klinik.antrean.ui.model.MetaData
import com.klinik.antrean.ui.model.Token
import com.klinik.antrean.ui.model.User

data class UserResponse(
    @SerializedName("response")
    var response: Token,
    @SerializedName("metadata")
    var metadata: MetaData
)
data class ReadResponse(
    @SerializedName("response")
    var response: User,
    @SerializedName("metadata")
    var metadata: MetaData
)

