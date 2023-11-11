package ru.testapp.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.netology.nmedia.util.SingleLiveEvent
import ru.testapp.nework.dto.Media
import ru.testapp.nework.dto.MediaUpload
import ru.testapp.nework.repository.RepositoryUsers
import ru.testapp.nework.state.StateRegisterUser
import java.io.File
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelSignUp @Inject constructor(
    private val repository: RepositoryUsers
) : ViewModel() {
    private val _registerImageState = MutableLiveData<MediaUpload?>()

    val registerImageState: LiveData<MediaUpload?>
        get() = _registerImageState


    private val _registerUserState = SingleLiveEvent<StateRegisterUser>()
    val registerUserState: LiveData<StateRegisterUser>
        get() = _registerUserState

    fun setRegisterImage(media: MediaUpload) {
        _registerImageState.value = media
    }

    fun clearPhoto() {
        _registerImageState.value = null
    }

    fun saveRegisteredUser(login: String, password: String, name: String, file: File) {
        viewModelScope.launch {
            try {
                repository.registerUser(login, name, password, file)
            } catch (e: Exception) {
                _registerUserState.value = StateRegisterUser(error = true)
            }
        }
    }
    fun saveRegisterUserWithoutAvatar(login: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                repository.registerUserWithoutAvatar(login, name, password)
            } catch (e: Exception) {
                _registerUserState.value = StateRegisterUser(error = true)
            }
        }
    }
}