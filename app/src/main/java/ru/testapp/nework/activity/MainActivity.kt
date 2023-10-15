package ru.testapp.nework.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.viewmodel.ViewModelAuth
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appAuth: AppAuth

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        toolbar = findViewById(R.id.toolbar)
//
//        setSupportActionBar(toolbar)

        val authViewModel by viewModels<ViewModelAuth>()

        var currentMenuProvider: MenuProvider? = null

        authViewModel.data.observe(this) {
            currentMenuProvider?.let { ::removeMenuProvider }
            addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_auth, menu)

                    val authenticated = authViewModel.authenticated

                    menu.let {
                        it.setGroupVisible(R.id.authorized, authenticated)
                        it.setGroupVisible(R.id.unauthorized, !authenticated)
                    }
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when (menuItem.itemId) {
                        R.id.signUp -> {
                            //TODO:
                            true
                        }

                        R.id.signIn -> {
                            // TODO:
                            true
                        }

                        R.id.logout -> {
                            // TODO:
                            true
                        }

                        else -> false
                    }
            }.also {
                currentMenuProvider = it
            }, this)
        }
    }
}