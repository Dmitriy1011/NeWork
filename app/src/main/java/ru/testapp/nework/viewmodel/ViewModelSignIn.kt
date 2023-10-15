package ru.testapp.nework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.testapp.nework.repository.RepositoryUsers
import javax.inject.Inject

@HiltViewModel
class ViewModelSignIn @Inject constructor(
    private val repository: RepositoryUsers
) : ViewModel() {
    fun saveIdAndToken(id: String, token: String) {
        viewModelScope.launch {
            try {
                repository.setIdAndTokenToAuth(id, token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}