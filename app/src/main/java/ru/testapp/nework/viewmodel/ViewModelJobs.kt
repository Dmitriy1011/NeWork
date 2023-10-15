package ru.testapp.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.util.SingleLiveEvent
import ru.testapp.nework.dto.Job
import ru.testapp.nework.models.ModelJobMy
import ru.testapp.nework.models.ModelJobUser
import ru.testapp.nework.repository.RepositoryJobs
import java.io.IOException
import javax.inject.Inject

private val emptyJob = Job(
    id = 0L,
    name = "",
    position = "",
    start = "",
    finish = null,
    link = null
)

@HiltViewModel
class ViewModelJobs @Inject constructor(
    private val repositoryJobs: RepositoryJobs,
) : ViewModel() {
    val jobDataMy: LiveData<ModelJobMy> =
        repositoryJobs.dataMyJobs.map(::ModelJobMy).asLiveData(Dispatchers.Default)
    val jobDataUser: LiveData<ModelJobUser> =
        repositoryJobs.dataMyJobs.map(::ModelJobUser).asLiveData(Dispatchers.Default)

    private val edited = MutableLiveData(emptyJob)

    private val _jobCreated = SingleLiveEvent<Unit>()
    val jobCreated: SingleLiveEvent<Unit>
        get() = _jobCreated

    init {
        loadJobsMy()
    }

    fun loadJobsMy() {
        viewModelScope.launch {
            try {
                repositoryJobs.getMyJobs()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    fun saveJob() {
        edited.value?.let {
            jobCreated.postValue(Unit)
            viewModelScope.launch {
                try {
                    repositoryJobs.saveJob(it)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
        edited.value = emptyJob
    }

    fun editJob(job: Job) {
        edited.value = job
    }

    fun changeContent(nameArg: String, positionArg: String) {
        val name = nameArg.trim()
        val position = positionArg.trim()
        if(edited.value?.name == name && edited.value?.position == position) {
            return
        } else if(edited.value?.name != name && edited.value?.position == position) {
            edited.value =
                edited.value?.copy(name = name)
        } else if(edited.value?.name == name && edited.value?.position != position) {
            edited.value =
                edited.value?.copy(position = position)
        }
    }

    fun removeJob(id: Long) {
        viewModelScope.launch {
            try {
                repositoryJobs.removeJob(id)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }
}