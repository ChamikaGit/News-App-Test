package com.chamika.newsapptest.data.repository

import com.chamika.newsapptest.data.api.NewsAPIService
import com.chamika.newsapptest.data.models.APIResponse
import com.chamika.newsapptest.data.models.Article
import com.chamika.newsapptest.data.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NewsRepositoryImpl(
    private val apiService: NewsAPIService
) :
    NewsRepository {

    override suspend fun getNewsHeadlines(country: String, page: Int): Resource<APIResponse> {

        return responseToResource(apiService.getTopHeadlines(country = country, page = page))
    }

    override suspend fun searchedNews(
        country: String,
        page: Int,
        searchQuery: String
    ): Resource<APIResponse> {
        return responseToResource(
            apiService.getSearchedTopHeadlines(
                country = country,
                page = page,
                query = searchQuery
            )
        )
    }

    private fun responseToResource(response: Response<APIResponse>): Resource<APIResponse> {

        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}