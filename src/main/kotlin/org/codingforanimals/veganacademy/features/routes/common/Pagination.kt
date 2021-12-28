package org.codingforanimals.veganacademy.features.routes.common

import com.google.gson.annotations.SerializedName

data class PaginationRequest(
    @SerializedName("page_size") val pageSize: Int,
    @SerializedName("page_number") val pageNumber: Int,
    @SerializedName("get_accepted") val getAccepted: Boolean,
)

data class PaginationResponse<T>(
    @SerializedName("has_more_content") val hasMoreContent: Boolean,
    @SerializedName("page_size") val pageSize: Int,
    @SerializedName("page_number") val pageNumber: Int,
    val content: List<T>
)