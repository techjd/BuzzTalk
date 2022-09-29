package com.ssip.buzztalk.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var navGraph: NavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

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
            }
        }
    }

    fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    fun hideBottomNavigationView() {
        binding.bottomNavigation.visibility = View.GONE
    }
}