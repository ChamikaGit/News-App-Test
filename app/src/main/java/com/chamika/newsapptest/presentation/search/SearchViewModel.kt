package com.chamika.newsapptest.presentation.search

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
class SearchViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {
    val isNetworkAvailable: MutableLiveData<Boolean> = MutableLiveData(true)

    private val _newsSearchLiveData: MutableLiveData<Resource<TopHeadlineResponse>> =
        MutableLiveData()
    var newsSearchLiveData: LiveData<Resource<TopHeadlineResponse>> = _newsSearchLiveData

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
        _newsSearchLiveData.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable.value == true) {
                val apiResult =
                    newsRepository.searchedNews(searchQuery = searchQuery, sortBy = sortBy)
                _newsSearchLiveData.postValue(apiResult)
            } else {
                _newsSearchLiveData.postValue(Resource.Error(message = "Internet not available"))
            }
        } catch (e: Exception) {
            _newsSearchLiveData.postValue(Resource.Error(e.message.toString()))
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