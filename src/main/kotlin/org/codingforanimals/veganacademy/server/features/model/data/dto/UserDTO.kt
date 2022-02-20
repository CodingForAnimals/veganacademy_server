package org.codingforanimals.veganacademy.server.features.model.data.dto

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName(USER_ID) val userId: Int,
    @SerializedName(USER_EMAIL) val email: String,
    @SerializedName(USER_DISPLAY_NAME) val displayName: String,
)

const val USER_ID = "user_id"
const val USER_EMAIL = "user_email"
const val USER_DISPLAY_NAME = "user_display_name"