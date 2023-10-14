package com.chamika.newsapptest.data.repository

import com.chamika.newsapptest.data.models.APIResponse
import com.chamika.newsapptest.data.util.Resource

interface NewsRepository {

    suspend fun getNewsHeadlines(country : String,page : Int) : Resource<APIResponse>
    suspend fun searchedNews(country : String,page : Int,searchQuery : String) : Resource<APIResponse>
}