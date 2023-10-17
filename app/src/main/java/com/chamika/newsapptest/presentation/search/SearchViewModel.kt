package com.chamika.newsapptest.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamika.newsapptest.data.models.TopHeadlineResponse
import com.chamika.newsapptest.data.repository.NewsRepository
import com.chamika.newsapptest.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {
    val isNetworkAvailable: MutableLiveData<Boolean> = MutableLiveData(true)

    private val _newsSearchSharedFlow: MutableSharedFlow<Resource<TopHeadlineResponse>> =
        MutableSharedFlow()
    var newsSearchSharedFlow: MutableSharedFlow<Resource<TopHeadlineResponse>> = _newsSearchSharedFlow

    private val sortByList: ArrayList<String> =
        arrayListOf(
            "publishedAt",
            "relevancy",
            "popularity",
            "relevancy"
        )

    var sortBy = sortByList[0]


    fun getCategoriesList(): ArrayList<String> {
        return sortByList
    }


    fun getNewsSearchData(
        searchQuery: String, sortBy: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        _newsSearchSharedFlow.emit(Resource.Loading())
        try {
            if (isNetworkAvailable.value == true) {
                val apiResult =
                    newsRepository.searchedNews(searchQuery = searchQuery, sortBy = sortBy)
                _newsSearchSharedFlow.emit(apiResult)
            } else {
                _newsSearchSharedFlow.emit(Resource.Error(message = "Internet not available"))
            }
        } catch (e: Exception) {
            _newsSearchSharedFlow.emit(Resource.Error(e.message.toString()))
        }
    }

//    fun getNewsCategories(country: String,category: String) = viewModelScope.launch(Dispatchers.IO) {
//        _newsCategoryLiveData.postValue(Resource.Loading())
//        try {
//            if (isNetworkAvailable.value == true) {
//                val apiResult = newsRepository.getCategoryNews(country = country, category = category)
//                _newsCategoryLiveData.postValue(apiResult)
//            } else {
//                _newsCategoryLiveData.postValue(Resource.Error(message = "Internet not available"))
//            }
//        } catch (e: Exception) {
//            _newsCategoryLiveData.postValue(Resource.Error(e.message.toString()))
//        }
//    }
}