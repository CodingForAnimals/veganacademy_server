package org.codingforanimals.veganacademy.server.features.model.dto

import com.google.gson.annotations.SerializedName

data class LoggedInUserDTO(
    @SerializedName("user_device_id") val userDeviceId: String,
    @SerializedName("user_token") val userToken: String,
)