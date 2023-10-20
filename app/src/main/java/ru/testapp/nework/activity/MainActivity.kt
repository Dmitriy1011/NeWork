package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.auth.AppAuth
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {

    @Inject
    lateinit var appAuth: AppAuth

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        //toolbar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.fragmentChooseUsers,
                R.id.fragmentCreateAndEditEvent,
                R.id.fragmentEventInDetails2,
                R.id.fragmentEvents,
                R.id.fragmentJobCreate,
                R.id.fragmentLikersEvent2,
                R.id.fragmentCreateAndEditPost,
                R.id.fragmentPostInDetails,
                R.id.fragmentPostsFeed,
                R.id.fragmentProfileMy,
                R.id.fragmentProfileUser,
                R.id.fragmentSignIn,
                R.id.fragmentSignUp,
                R.id.fragmentUsers
            ),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)

        setSupportActionBar(toolbar)

        //bottomNavigation
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav)

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
}