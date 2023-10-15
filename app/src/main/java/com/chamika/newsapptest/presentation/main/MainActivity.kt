package com.chamika.newsapptest.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.chamika.newsapptest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.delayCompleted.value == true
            }
        }
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.delayTheSplash {
                mainViewModel.closeSplash()
            }
        }

    }
}