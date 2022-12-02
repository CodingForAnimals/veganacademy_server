package org.codingforanimals.veganacademy.server.features.model.data.dto

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user_email") val email: String,
    @SerializedName("user_display_name") val displayName: String,
)