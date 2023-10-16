package com.chamika.newsapptest.data.repository

import com.chamika.newsapptest.data.api.NewsAPIService
import com.chamika.newsapptest.data.models.TopHeadlineResponse
import com.chamika.newsapptest.data.util.Resource
import retrofit2.Response

class NewsRepositoryImpl(
    private val apiService: NewsAPIService
) :
    NewsRepository {

    override suspend fun getNewsHeadlines(country: String): Resource<TopHeadlineResponse> {
        return responseToResource(apiService.getTopHeadlines(country = country))
    }

    override suspend fun searchedNews(
        country: String,
        page: Int,
        searchQuery: String
    ): Resource<TopHeadlineResponse> {
        return responseToResource(
            apiService.getSearchedTopHeadlines(
                country = country,
                page = page,
                query = searchQuery
            )
        )
    }

    private fun responseToResource(response: Response<TopHeadlineResponse>): Resource<TopHeadlineResponse> {

        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}