package com.chamika.newsapptest.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamika.newsapptest.data.repository.data_store_preferences.DataStorePreferencesRepository
import com.chamika.newsapptest.data.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStorePreferences: DataStorePreferencesRepository
) : ViewModel() {


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

    fun getLoginStatus(): Boolean? = runBlocking {
        dataStorePreferences.getBoolean(Constant.isUserLogged)
    }


}