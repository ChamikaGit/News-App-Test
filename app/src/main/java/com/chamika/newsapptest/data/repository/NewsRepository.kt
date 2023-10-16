package com.chamika.newsapptest.data.repository

import com.chamika.newsapptest.data.models.TopHeadlineResponse
import com.chamika.newsapptest.data.util.Resource

interface NewsRepository {

    suspend fun getNewsHeadlines(country : String) : Resource<TopHeadlineResponse>
    suspend fun searchedNews(country : String,page : Int,searchQuery : String) : Resource<TopHeadlineResponse>
}