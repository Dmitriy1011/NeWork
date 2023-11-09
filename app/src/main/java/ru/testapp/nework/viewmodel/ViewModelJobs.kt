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
import ru.testapp.nework.dto.Job
import ru.testapp.nework.models.ModelJobMy
import ru.testapp.nework.models.ModelJobUser
import ru.testapp.nework.repository.RepositoryJobs
import ru.testapp.nework.state.StateJobMy
import ru.testapp.nework.state.StateJobUser
import java.time.format.DateTimeFormatter
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
@ExperimentalCoroutinesApi
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


    private val _startDateState = MutableLiveData<String?>()
    val startDateState: LiveData<String?>
        get() = _startDateState

    private val _endDateState = MutableLiveData<String?>()
    val endDateState: LiveData<String?>
        get() = _endDateState


    private val _userIdState = MutableLiveData<Int>()
    val userIdState: LiveData<Int>
        get() = _userIdState


    fun setUserIdState(id: Long) {
        _userIdState.value = id.toInt()
    }


    fun editStartDate(startDate: String) {
        _startDateState.value = startDate
    }

    fun editEndDate(endDate: String) {
        _endDateState.value = endDate
    }

    private val _loadingJobMyState = MutableLiveData(StateJobMy())
    val loadingJobMyState: LiveData<StateJobMy>
        get() = _loadingJobMyState


    private val _loadingJobUserState = MutableLiveData(StateJobUser())
    val loadingJobUserState: LiveData<StateJobUser>
        get() = _loadingJobUserState

    init {
        loadJobsMy()
    }

    fun loadJobsMy() {
        viewModelScope.launch {
            _loadingJobMyState.value = StateJobMy(loading = true)
            try {
                repositoryJobs.getMyJobs()
                _loadingJobMyState.value = StateJobMy()
            } catch (e: Exception) {
                _loadingJobMyState.value = StateJobMy(error = true)
            }
        }
    }


    fun loadJobsUser(id: Long) {
        viewModelScope.launch {
            _loadingJobUserState.value = StateJobUser(loading = true)
            try {
                repositoryJobs.getUsersJobs(id)
                _loadingJobUserState.value = StateJobUser()
            } catch (e: Exception) {
                _loadingJobUserState.value = StateJobUser(error = true)
            }
        }
    }

    fun saveJob() {
        edited.value?.let {
            jobCreated.postValue(Unit)
            viewModelScope.launch {
                _loadingJobMyState.value = StateJobMy(loading = true)
                try {
                    repositoryJobs.saveJob(it)
                    _loadingJobMyState.value = StateJobMy()
                    jobCreated.postValue(Unit)
                    edited.value = emptyJob
                } catch (e: Exception) {
                    _loadingJobMyState.value = StateJobMy(error = true)
                }
            }
        }
        edited.value = emptyJob
    }

    private val serverDateFormat = DateTimeFormatter.ISO_DATE
    private val localDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun changeContent(
        nameArg: String,
        positionArg: String,
        link: String?,
        dateStart: String,
        dateFinish: String?
    ) {
        val name = nameArg.trim()
        val position = positionArg.trim()
        val link = link?.trim()
        val start = dateStart.trim()
        val finish = dateFinish?.trim()

        edited.value =
            edited.value?.copy(
                name = nameArg.trim(),
                position = positionArg.trim(),
                link = link?.trim()?.takeIf { it.isNotBlank() },
                start = serverDateFormat.format(localDateFormat.parse(dateStart.trim())),
                finish = dateFinish?.trim()?.let {
                    serverDateFormat.format(localDateFormat.parse(it))
                }
            )

        if (edited.value?.name == name && edited.value?.position == position) {
            return

        } else if (edited.value?.name != name && edited.value?.position == position) {
            edited.value =
                edited.value?.copy(name = name)

        } else if (edited.value?.name == name && edited.value?.position != position) {
            edited.value =
                edited.value?.copy(position = position)

        } else if (edited.value?.name == name && edited.value?.position == position && edited.value?.link != link) {
            edited.value =
                edited.value?.copy(link = link)
        }
    }

    fun removeJob(id: Long) {
        viewModelScope.launch {
            _loadingJobMyState.value = StateJobMy(loading = true)
            try {
                repositoryJobs.removeJob(id)
                _loadingJobMyState.value = StateJobMy()
            } catch (e: Exception) {
                _loadingJobMyState.value = StateJobMy(error = true)
            }
        }
    }
}