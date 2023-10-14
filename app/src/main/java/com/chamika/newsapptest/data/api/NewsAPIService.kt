package com.chamika.newsapptest.data.api

import com.chamika.newsapptest.BuildConfig
import com.chamika.newsapptest.data.models.APIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        country: String,
        @Query("page")
        page: Int,
        @Query("apiKey")
        apiKey: String = BuildConfig.API_KEY
    ): Response<APIResponse>

    @GET("top-headlines")
    suspend fun getSearchedTopHeadlines(
        @Query("country")
        country: String,
        @Query("page")
        page: Int,
        @Query("q")
        query : String,
        @Query("apiKey")
        apiKey: String = BuildConfig.API_KEY
    ): Response<APIResponse>

}