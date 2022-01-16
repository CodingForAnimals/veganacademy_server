package org.codingforanimals.veganacademy.server.features.routes.user

import com.google.gson.annotations.SerializedName

data class UserLoginRequest(
    @SerializedName("user_email") val email: String,
    @SerializedName("user_password") val password: String,
)

data class UserRegisterRequest(
    @SerializedName("user_email") val email: String,
    @SerializedName("user_password") val password: String,
    @SerializedName("user_display_name") val displayName: String,
)