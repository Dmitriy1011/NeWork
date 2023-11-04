package ru.testapp.nework.auth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val tokenKey = "TOKEN_KEY"
    private val idKey = "ID_KEY"

    data class AuthState(
        val id: Long = 0,
        val token: String? = null
    )

    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val token = prefs.getString(tokenKey, null)
        val id = prefs.getLong(idKey, 0L)

        if (id == 0L || token == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(AuthState(id, token))
        }
    }

    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()


    @Synchronized
    fun setAuth(id: Long, token: String) {
        _authStateFlow.value = AuthState(id, token)
        with(prefs.edit()) {
            putLong(idKey, id)
            putString(tokenKey, token)
            apply()
        }
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        with(prefs.edit()) {
            clear()
            apply()
        }
    }

    companion object {
        @Volatile
        private var instance: AppAuth? = null

        fun getInstance(): AppAuth = synchronized(this) {
            instance ?: throw IllegalStateException(
                "AppAuth is not initialized, you must call AppAuth.initializeApp(Context context) first."
            )
        }

        fun initApp(context: Context): AppAuth = instance ?: synchronized(this) {
            instance ?: buildAuth(context).also { instance = it }
        }

        private fun buildAuth(context: Context): AppAuth = AppAuth(context)
    }
}