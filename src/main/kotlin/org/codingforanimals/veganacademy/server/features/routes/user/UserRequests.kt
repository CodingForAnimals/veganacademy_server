package org.codingforanimals.veganacademy.server.features.routes.user

import com.google.gson.annotations.SerializedName
import org.codingforanimals.veganacademy.server.features.model.dto.UserDTO

data class UserLoginRequest(
    @SerializedName("user_device_uuid") val deviceUUID: String,
    @SerializedName("user_email") val email: String,
    @SerializedName("user_password") val password: String,
)

data class UserRegisterRequest(
    @SerializedName("user_device_uuid") val deviceUUID: String,
    @SerializedName("user_email") val email: String,
    @SerializedName("user_password") val password: String,
    @SerializedName("user_display_name") val displayName: String,
)

data class UserLoginRegisterResponse(
    @SerializedName("remember_me_token") val rememberMeToken: String?,
    val user: UserDTO,
)