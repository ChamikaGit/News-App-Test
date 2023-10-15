package com.chamika.newsapptest.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.chamika.newsapptest.R
import com.chamika.newsapptest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private val TAG = "MainActivity"


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

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavigationView,navController)
        handleNavGraphDirection(navController)

    }

    private fun handleNavGraphDirection(navController: NavController) {
        val isUserLogged = mainViewModel.getLoginStatus()
        Log.e(TAG, "login status $isUserLogged")

        val navGraphId = if (isUserLogged == false) {
            R.navigation.login_nav_graph
        } else {
            R.navigation.dashboard_nav_graph
        }

        navController.graph = navController.navInflater.inflate(navGraphId)

        if (navGraphId == R.navigation.dashboard_nav_graph) {
            navController.graph.setStartDestination(R.id.homeFragment)
        } else {
            navController.graph.setStartDestination(R.id.loginFragment)
//            navController.setGraph(
//                navGraphId,
//                LanguageFragmentArgs(isFromSettings = false).toBundle()
//            )
            navController.setGraph(navGraphId)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.favouriteFragment -> {
                    bottomNavigationBarVisibility(isVisible = true)
                }
                R.id.homeFragment -> {
                    bottomNavigationBarVisibility(isVisible = true)
                }
                R.id.profileFragment -> {
                    bottomNavigationBarVisibility(isVisible = true)
                }
                R.id.searchFragment -> {
                    bottomNavigationBarVisibility(isVisible = false)
                }
                else -> {
                    bottomNavigationBarVisibility(isVisible = false)
                }
            }
        }

    }

    private fun bottomNavigationBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else {
            binding.bottomNavigationView.visibility = View.GONE
        }
    }
}