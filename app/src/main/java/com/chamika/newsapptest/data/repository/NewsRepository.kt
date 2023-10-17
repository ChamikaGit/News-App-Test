package com.chamika.newsapptest.data.repository

import com.chamika.newsapptest.data.models.TopHeadlineResponse
import com.chamika.newsapptest.data.util.Resource

interface NewsRepository {

    suspend fun getNewsHeadlines(country: String): Resource<TopHeadlineResponse>
    suspend fun getCategoryNews(country: String, category: String): Resource<TopHeadlineResponse>
    suspend fun searchedNews(searchQuery: String, sortBy: String): Resource<TopHeadlineResponse>
}