package com.chamika.newsapptest.presentation.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamika.newsapptest.data.models.ArticleX
import com.chamika.newsapptest.data.models.TopHeadlineResponse
import com.chamika.newsapptest.data.repository.NewsRepository
import com.chamika.newsapptest.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    val isNetworkAvailable: MutableLiveData<Boolean> = MutableLiveData(true)

    private val _newsHeadlineSharedFlow: MutableSharedFlow<Resource<TopHeadlineResponse>> =
        MutableSharedFlow()
    var newsHeadlineSharedFlow: MutableSharedFlow<Resource<TopHeadlineResponse>> = _newsHeadlineSharedFlow

    private val _newsCategorySharedFlow: MutableSharedFlow<Resource<TopHeadlineResponse>> =
        MutableSharedFlow()
    var newsCategorySharedFlow: MutableSharedFlow<Resource<TopHeadlineResponse>> = _newsCategorySharedFlow

    private val _hotNewsListLiveData = MutableLiveData<List<ArticleX>>()
    val hotNewsListLiveData: LiveData<List<ArticleX>> = _hotNewsListLiveData

    private val categoriesList: ArrayList<String> =
        arrayListOf(
            "business",
            "entertainment",
            "general",
            "health",
            "science",
            "sports",
            "technology"
        )

    var categoryType = categoriesList[0]


    fun getCategoriesList(): ArrayList<String> {
        return categoriesList
    }


    fun getNewsHeadLines(country: String) = viewModelScope.launch(Dispatchers.IO) {
        _newsHeadlineSharedFlow.emit(Resource.Loading())
        try {
            if (isNetworkAvailable.value == true) {
                val apiResult = newsRepository.getNewsHeadlines(country = country)
                _newsHeadlineSharedFlow.emit(apiResult)
            } else {
                _newsHeadlineSharedFlow.emit(Resource.Error(message = "Internet not available"))
            }
        } catch (e: Exception) {
            _newsHeadlineSharedFlow.emit(Resource.Error(e.message.toString()))
        }
    }

    fun getNewsCategories(country: String,category: String) = viewModelScope.launch(Dispatchers.IO) {
        _newsCategorySharedFlow.emit(Resource.Loading())
        try {
            if (isNetworkAvailable.value == true) {
                val apiResult = newsRepository.getCategoryNews(country = country, category = category)
                _newsCategorySharedFlow.emit(apiResult)
            } else {
                _newsCategorySharedFlow.emit(Resource.Error(message = "Internet not available"))
            }
        } catch (e: Exception) {
            _newsCategorySharedFlow.emit(Resource.Error(e.message.toString()))
        }
    }


    fun setHotNewsList(yourList: List<ArticleX>) {
        _hotNewsListLiveData.value = yourList
    }
}