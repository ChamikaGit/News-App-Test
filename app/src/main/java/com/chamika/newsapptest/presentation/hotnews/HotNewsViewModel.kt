package com.chamika.newsapptest.presentation.hotnews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chamika.newsapptest.data.models.ArticleX
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HotNewsViewModel @Inject constructor() : ViewModel() {

    private val _hotNewsListLiveData = MutableLiveData<List<ArticleX>>()
    val hotNewsListLiveData: LiveData<List<ArticleX>> = _hotNewsListLiveData


    fun setHotNewsList(yourList: List<ArticleX>) {
        _hotNewsListLiveData.value = yourList
    }


}