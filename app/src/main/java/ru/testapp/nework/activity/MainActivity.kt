package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appAuth: AppAuth

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        val toolbar = binding.toolbar

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)

        val navView: BottomNavigationView = binding.bottomNav

        val bottomAppBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.fragmentPostsFeed,
                R.id.fragmentEvents,
                R.id.fragmentUsers
            )
        )
        setupActionBarWithNavController(navController, bottomAppBarConfiguration)
        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragmentUsers || destination.id == R.id.fragmentEvents || destination.id == R.id.fragmentPostsFeed) {
                navView.visibility = View.VISIBLE
            } else {
                navView.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}