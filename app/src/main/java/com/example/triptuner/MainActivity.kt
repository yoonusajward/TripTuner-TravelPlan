package com.example.triptuner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.triptuner.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Set up NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up the action bar with the nav controller
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.itinerariesFragment, R.id.userProfileFragment, R.id.navigation_budget_planner)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up Bottom Navigation with NavController
        setupBottomNavigation()

        // Hide Bottom Navigation on specific fragments
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.welcomeFragment, R.id.loginFragment, R.id.signUpFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    // Always navigate to HomeFragment, resetting the back stack
                    navController.popBackStack(R.id.homeFragment, true)
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.itinerariesFragment -> {
                    // Always navigate to ItinerariesFragment, resetting the back stack
                    navController.popBackStack(R.id.itinerariesFragment, true)
                    navController.navigate(R.id.itinerariesFragment)
                    true
                }
                R.id.userProfileFragment -> {
                    // Always navigate to UserProfileFragment, resetting the back stack
                    navController.popBackStack(R.id.userProfileFragment, true)
                    navController.navigate(R.id.userProfileFragment)
                    true
                }
                R.id.navigation_budget_planner -> {
                    // Always navigate to BudgetPlannerFragment, resetting the back stack
                    navController.popBackStack(R.id.navigation_budget_planner, true)
                    navController.navigate(R.id.navigation_budget_planner)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
