package com.chamika.newsapptest.presentation.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamika.newsapptest.data.models.TopHeadlineResponse
import com.chamika.newsapptest.data.repository.NewsRepository
import com.chamika.newsapptest.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    val isNetworkAvailable: MutableLiveData<Boolean> = MutableLiveData(true)

    private val _newsHeadlineLiveData: MutableLiveData<Resource<TopHeadlineResponse>> =
        MutableLiveData()
    var newsHeadlineLiveData: LiveData<Resource<TopHeadlineResponse>> = _newsHeadlineLiveData

    private val _newsCategoryLiveData: MutableLiveData<Resource<TopHeadlineResponse>> =
        MutableLiveData()
    var newsCategoryLiveData: LiveData<Resource<TopHeadlineResponse>> = _newsCategoryLiveData

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
        _newsHeadlineLiveData.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable.value == true) {
                val apiResult = newsRepository.getNewsHeadlines(country = country)
                _newsHeadlineLiveData.postValue(apiResult)
            } else {
                _newsHeadlineLiveData.postValue(Resource.Error(message = "Internet not available"))
            }
        } catch (e: Exception) {
            _newsHeadlineLiveData.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun getNewsCategories(country: String,category: String) = viewModelScope.launch(Dispatchers.IO) {
        _newsCategoryLiveData.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable.value == true) {
                val apiResult = newsRepository.getCategoryNews(country = country, category = category)
                _newsCategoryLiveData.postValue(apiResult)
            } else {
                _newsCategoryLiveData.postValue(Resource.Error(message = "Internet not available"))
            }
        } catch (e: Exception) {
            _newsCategoryLiveData.postValue(Resource.Error(e.message.toString()))
        }
    }
}