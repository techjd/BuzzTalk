package com.ssip.buzztalk.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ActivityMainBinding
import com.ssip.buzztalk.ui.fragments.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var navGraph: NavGraph

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
        navGraph = navController.navInflater.inflate(R.navigation.nav_graph)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.splashFragment -> {
                    hideBottomNavigationView()
                }
                R.id.chooseLoginSignUpFragment -> {
                    hideBottomNavigationView()
                }
                R.id.loginFragment -> {
                    hideBottomNavigationView()
                }
                R.id.signUpFragment -> {
                    hideBottomNavigationView()
                }
                R.id.addEmailFragment -> {
                    hideBottomNavigationView()
                }
                R.id.addFirstLastNameFragment -> {
                    hideBottomNavigationView()
                }
                R.id.addPasswordFragment -> {
                    hideBottomNavigationView()
                }
                R.id.homeFragment -> {
                    showBottomNavigation()
                }
                R.id.profileFragment -> {
                    hideBottomNavigationView()
                }
                R.id.chatsFragment -> {
                    hideBottomNavigationView()
                }
                R.id.postFragment -> {
                    hideBottomNavigationView()
                }
                R.id.searchFragment -> {
                    showBottomNavigation()
                }
                R.id.notificationsFragment -> {
                    showBottomNavigation()
                }
                R.id.jobsFragment -> {
                    showBottomNavigation()
                }
                R.id.userDetailProfileFragment -> {
                    hideBottomNavigationView()
                }
                R.id.connectionRequestsFragment -> {
                    hideBottomNavigationView()
                }
                R.id.detailedPostFragment -> {
                    hideBottomNavigationView()
                }
            }
        }
    }

    private fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNavigationView() {
        binding.bottomNavigation.visibility = View.GONE
    }

    fun setStartDestinationAsHomeFragment() {
        navGraph.setStartDestination(R.id.homeFragment)
        navController.graph = navGraph
    }

    fun setStartDestinationAsSplashFragment() {
        navGraph.setStartDestination(R.id.splashFragment)
        navController.graph = navGraph
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.homeFragment) {
            super.onBackPressed()
            chatViewModel.removeMeOnline()
        } else {
            super.onBackPressed()
        }
    }
}