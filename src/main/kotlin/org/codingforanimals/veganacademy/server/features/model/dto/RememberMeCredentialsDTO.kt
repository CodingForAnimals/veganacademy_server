package org.codingforanimals.veganacademy.server.features.model.dto

import com.google.gson.annotations.SerializedName

data class RememberMeCredentialsDTO(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user_device_id") val userDeviceUUID: String,
    @SerializedName("user_token") val userToken: String,
)