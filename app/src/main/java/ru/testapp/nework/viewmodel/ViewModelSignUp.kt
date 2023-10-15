package ru.testapp.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.testapp.nework.dto.MediaUpload
import ru.testapp.nework.repository.RepositoryUsers
import java.io.File
import javax.inject.Inject

@HiltViewModel
class
ViewModelSignUp @Inject constructor(
    private val repository: RepositoryUsers
) : ViewModel() {
    private val _registerImage = MutableLiveData<MediaUpload>()

    val registerImage: LiveData<MediaUpload>
        get() = _registerImage

    fun setRegisterImage(media: MediaUpload) {
        _registerImage.value = media
    }

    fun saveRegisteredUser(login: String, password: String, name: String, file: File) {
        viewModelScope.launch {
            try {
                repository.registerUser(login, name, password, file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}