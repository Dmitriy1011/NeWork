package ru.testapp.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.util.SingleLiveEvent
import ru.testapp.nework.models.ModelUser
import ru.testapp.nework.repository.RepositoryUsers
import ru.testapp.nework.state.StateUserModel
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelUsers @Inject constructor(
    private val repositoryUsers: RepositoryUsers
) : ViewModel() {
    val data: LiveData<ModelUser> =
        repositoryUsers.data.map(::ModelUser).asLiveData(Dispatchers.Default)

    private val _state = MutableLiveData<StateUserModel>()
    val state: LiveData<StateUserModel>
        get() = _state

    private val _usersLoadError = SingleLiveEvent<String>()
    val usersLoadError: SingleLiveEvent<String>
        get() = _usersLoadError

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.value = StateUserModel(loading = true)
            try {
                repositoryUsers.getAllUsers()
                _state.value = StateUserModel()
            } catch (e: Exception) {
                _state.value = StateUserModel(error = true)
            }
        }
    }
}