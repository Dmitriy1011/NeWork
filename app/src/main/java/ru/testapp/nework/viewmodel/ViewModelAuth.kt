package ru.testapp.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.dto.PhotoModel
import javax.inject.Inject

@HiltViewModel
class ViewModelAuth @Inject constructor(
    private val auth: AppAuth
) : ViewModel() {
    val data = auth.authStateFlow.asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = auth.authStateFlow.value.id != 0L
}