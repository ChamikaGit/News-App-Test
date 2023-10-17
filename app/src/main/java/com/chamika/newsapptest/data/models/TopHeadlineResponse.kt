package com.chamika.newsapptest.data.models
import com.google.gson.annotations.SerializedName

data class TopHeadlineResponse(
    @SerializedName("articles")
    val articles: List<ArticleX>?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalResults")
    val totalResults: Int?
)