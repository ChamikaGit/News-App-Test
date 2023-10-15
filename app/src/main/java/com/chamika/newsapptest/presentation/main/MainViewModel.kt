package com.chamika.newsapptest.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {


    private val _delayCompleted = MutableLiveData(false)
    val delayCompleted: LiveData<Boolean> = _delayCompleted

    init {
        viewModelScope.launch {
            _delayCompleted.value = true
        }
    }

    suspend fun delayTheSplash(loaded: () -> Unit) {
        delay(2000)
        loaded.invoke()
    }

    fun closeSplash() {
        viewModelScope.launch {
            _delayCompleted.value = false
        }
    }


}