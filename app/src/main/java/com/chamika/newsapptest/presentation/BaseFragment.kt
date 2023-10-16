package com.chamika.newsapptest.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.chamika.newsapptest.data.util.ConnectionLiveData

open class BaseFragment : Fragment() {

    protected lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(requireActivity())
    }
}