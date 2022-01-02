package org.codingforanimals.veganacademy.server.features.routes.common

import com.google.gson.annotations.SerializedName

data class PaginationRequest<T>(
    @SerializedName("page_size") val pageSize: Int,
    @SerializedName("page_number") val pageNumber: Int,
    val filter: T,
)

data class PaginationResponse<T>(
    @SerializedName("has_more_content") val hasMoreContent: Boolean,
    @SerializedName("result_size") val resultSize: Int?,
    @SerializedName("page_size") val pageSize: Int,
    @SerializedName("page_number") val pageNumber: Int,
    val result: T
)