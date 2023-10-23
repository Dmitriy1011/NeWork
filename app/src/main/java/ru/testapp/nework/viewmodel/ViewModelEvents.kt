package ru.testapp.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.util.SingleLiveEvent
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.dto.Event
import ru.testapp.nework.dto.PhotoModel
import ru.testapp.nework.models.ModelEvent
import ru.testapp.nework.repository.RepositoryEvents
import ru.testapp.nework.state.StateEventModel
import javax.inject.Inject

private val emptyEvent = Event(
    id = 0,
    authorId = 0,
    author = "",
    authorAvatar = null,
    authorJob = "",
    content = "",
    datetime = "",
    published = "",
    coordinates = null,
    type = "",
    likeOwnerIds = null,
    likedByMe = false,
    speakerIds = null,
    participantsIds = null,
    participatedByMe = false,
    attachment = null,
    link = "",
    ownedByMe = false,
    users = emptyMap()
)

@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelEvents @Inject constructor(
    private val repository: RepositoryEvents,
    appAuth: AppAuth
) : ViewModel() {

    private val cached: Flow<PagingData<Event>> = repository.data.cachedIn(viewModelScope)

    val data: Flow<PagingData<Event>> = appAuth
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            cached.map { pagingData ->
                pagingData.map { pagingDataItem ->
                    if (pagingDataItem !is Event) pagingDataItem else pagingDataItem.copy(ownedByMe = pagingDataItem.authorId == myId)
                }
            }
        }

    private val _eventsState = MutableLiveData(StateEventModel())
    val eventsState: LiveData<StateEventModel>
        get() = _eventsState

    private val _eventCreated = SingleLiveEvent<Unit>()
    val eventCreated: SingleLiveEvent<Unit>
        get() = _eventCreated

    private val _eventEdited = SingleLiveEvent<Unit>()
    val eventEdited: SingleLiveEvent<Unit>
        get() = _eventEdited


    private val _eventLoadError = SingleLiveEvent<String>()
    val eventLoadError: SingleLiveEvent<String>
        get() = _eventLoadError

    private val _eventSaveError = SingleLiveEvent<String>()
    val eventSaveError: SingleLiveEvent<String>
        get() = _eventSaveError

    private val _photoState = MutableLiveData<PhotoModel?>()
    val photoState: LiveData<PhotoModel?>
        get() = _photoState

    private val edited = MutableLiveData(emptyEvent)


    private val _eventTypesState = MutableLiveData<String>()
    val eventTypesState: LiveData<String>
        get() = _eventTypesState

    private val _dateTimeState = MutableLiveData<String>()
    val dateTimeState: LiveData<String>
        get() = _dateTimeState

    fun editType(type: String) {
        _eventTypesState.value = type
    }

    fun editDateTime(dateTime: String) {
        _dateTimeState.value = dateTime
    }

    val eventDetailsData: LiveData<ModelEvent> =
        repository.detailsData.map(::ModelEvent).asLiveData(Dispatchers.Default)

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _eventsState.value = StateEventModel(loading = true)
            try {
                repository.getAllEvents()
                _eventsState.value = StateEventModel()
            } catch (e: Exception) {
                _eventsState.value = StateEventModel(error = true)
            }
        }
    }

    fun saveEvent() {
        edited.value?.let {
            eventCreated.postValue(Unit)
            viewModelScope.launch {
                _eventsState.value = StateEventModel(loading = true)
                try {
                    _photoState.value?.let { photoModel ->
                        repository.saveEventWithAttachment(it, photoModel.file)
                    } ?: run {
                        repository.saveEvent(it)
                    }
                    repository.saveEvent(it)
                    _eventsState.value = StateEventModel()
                } catch (e: Exception) {
                    _eventsState.value = StateEventModel(error = true)
                }
            }
        }

        edited.value = emptyEvent
    }


    fun likeEvent(id: Long) {
        viewModelScope.launch {
            _eventsState.value = StateEventModel(loading = true)
            try {
                repository.like(id)
                _eventsState.value = StateEventModel()
            } catch (e: Exception) {
                _eventsState.value = StateEventModel(error = true)
            }
        }
    }

    fun unLikeEvent(id: Long) {
        viewModelScope.launch {
            _eventsState.value = StateEventModel(loading = true)
            try {
                repository.unLike(id)
                _eventsState.value = StateEventModel()
            } catch (e: Exception) {
                _eventsState.value = StateEventModel(error = true)
            }
        }
    }


    fun editEvent(event: Event) {
        edited.value = event
    }

    fun changeContent(content: String, date: String, eventType: String) {
        val text = content.trim()
        val date = date.trim()
        val type = eventType.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value =
            edited.value?.copy(content = text, datetime = date, type = eventType)
    }

    fun removeEvent(id: Long) {
        viewModelScope.launch {
            _eventsState.value = StateEventModel(
                loading = true
            )
            try {
                repository.removeEvent(id)
                _eventsState.value = StateEventModel()
            } catch (e: Exception) {
                _eventsState.value = StateEventModel(error = true)
            }
        }
    }

    fun setPhoto(photoModel: PhotoModel) {
        _photoState.value = photoModel
    }

    fun clearPhoto() {
        _photoState.value = null
    }
}