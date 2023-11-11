package ru.testapp.nework.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.netology.nmedia.util.SingleLiveEvent
import ru.testapp.nework.R
import ru.testapp.nework.repository.RepositoryUsers
import ru.testapp.nework.state.StateAuth
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelSignIn @Inject constructor(
    private val repository: RepositoryUsers
) : ViewModel() {
    private val _wrongDataErrorState = MutableLiveData<StateAuth>()
    val wrongDataErrorState: LiveData<StateAuth>
        get() = _wrongDataErrorState
    fun saveIdAndToken(id: String, token: String) {
        viewModelScope.launch {
            _wrongDataErrorState.value = StateAuth(loading = true)
            try {
                _wrongDataErrorState.value = StateAuth()
                repository.setIdAndTokenToAuth(id, token)
            } catch (e: Exception) {
                _wrongDataErrorState.value = StateAuth(error = true)
            }
        }
    }
}