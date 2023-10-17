package com.chamika.newsapptest.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamika.newsapptest.BuildConfig
import com.chamika.newsapptest.data.repository.data_store_preferences.DataStorePreferencesRepository
import com.chamika.newsapptest.data.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStorePreferencesRepo: DataStorePreferencesRepository,
) : ViewModel() {

    private val TAG = "LoginViewModel"
    private fun getUserEmail(): String? = runBlocking {
        dataStorePreferencesRepo.getString(Constant.userEmail)
    }

    private fun getUserPassword(): String? = runBlocking {
        dataStorePreferencesRepo.getString(Constant.password)
    }

    fun isValidUser(enteredPw: String, enteredEmail: String): Boolean {
        if (BuildConfig.DEBUG){
            Log.e(TAG, "isValidUser: enteredPw $enteredPw enteredEmail $enteredEmail")
            Log.e(TAG, "isValidUser: pw ${getUserPassword()} email ${getUserEmail()}")
        }
        return enteredPw == getUserPassword() && enteredEmail == getUserEmail()
    }

    fun validateInputUserData(userEmail: String, userPW: String): Pair<Boolean, String> {
        if (userEmail.isEmpty()) {
            return Pair(first = false, second = "Please enter valid user email")
        }
        if (userPW.isEmpty()) {
            return Pair(first = false, second = "Please enter valid user password")
        }
        return Pair(first = true, "User Successfully Logged")
    }

    fun setUserLoginStatus(isLogged : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreferencesRepo.putBoolean(Constant.isUserLogged, isLogged)
        }
    }
}