package com.chamika.newsapptest.di

import com.chamika.newsapptest.data.api.NewsAPIService
import com.chamika.newsapptest.data.repository.NewsRepository
import com.chamika.newsapptest.data.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(apiService: NewsAPIService) : NewsRepository {
        return NewsRepositoryImpl(apiService = apiService)
    }

}