package com.chamika.newsapptest.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamika.newsapptest.data.repository.data_store_preferences.DataStorePreferencesRepository
import com.chamika.newsapptest.data.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegistrationViewModel  @Inject constructor(
    private val dataStorePreferencesRepo: DataStorePreferencesRepository,
) : ViewModel() {


    fun setUserEmail(userEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreferencesRepo.putString(Constant.userEmail, userEmail)
        }
    }

    fun setPassword(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreferencesRepo.putString(Constant.password, password)
        }
    }

    fun validateUserData(userName : String,userEmail : String,userPW : String) : Pair<Boolean,String>{
        if (userName.isEmpty()){
            return Pair(first = false, second ="Please enter valid user name" )
        }
        if (userEmail.isEmpty()){
            return Pair(first = false, second ="Please enter valid user email" )
        }
        if (userPW.isEmpty()){
            return Pair(first = false, second ="Please enter valid user password" )
        }
        return Pair(first = true,"User Successfully registered")
    }


}