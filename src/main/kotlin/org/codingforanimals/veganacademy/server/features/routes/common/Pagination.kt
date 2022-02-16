package org.codingforanimals.veganacademy.server.features.routes.common

import com.google.gson.annotations.SerializedName

data class PaginationRequest<T>(
    @SerializedName("pagination_info") val paginationInfo: PaginationInfo,
    val filter: T,
)

data class PaginationResponse<T, W>(
    @SerializedName("pagination_info") val paginationInfo: PaginationInfo,
    val filter: T,
    val result: List<W>,
)

data class PaginationInfo(
    @SerializedName("page_size") val pageSize: Int,
    @SerializedName("page_number") val pageNumber: Int,
    @SerializedName("has_more_content") val hasMoreContent: Boolean? = null,
    @SerializedName("result_size") val resultSize: Int? = null,
)