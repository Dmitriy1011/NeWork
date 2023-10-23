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
import ru.testapp.nework.dto.PhotoModel
import ru.testapp.nework.dto.Post
import ru.testapp.nework.models.ModelPost
import ru.testapp.nework.repository.RepositoryPost
import ru.testapp.nework.state.StateFeed
import javax.inject.Inject

private val emptyPost = Post(
    id = 0,
    authorId = 0,
    author = "",
    authorAvatar = null,
    authorJob = null,
    content = "",
    published = "",
    coordinates = null,
    link = null,
    likeOwnerIds = null,
    mentionIds = null,
    mentionedMe = false,
    likedByMe = false,
    attachment = null,
    ownedByMe = false,
    users = emptyMap(),
    likes = 0
)

@HiltViewModel
@ExperimentalCoroutinesApi
class ViewModelPost @Inject constructor(
    private val repository: RepositoryPost,
    appAuth: AppAuth
) : ViewModel() {

    private val cached: Flow<PagingData<Post>> = repository.data.cachedIn(viewModelScope)

    val data: Flow<PagingData<Post>> = appAuth
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            cached.map { pagingData ->
                pagingData.map { pagingDataItem ->
                    if (pagingDataItem !is Post) pagingDataItem else pagingDataItem.copy(ownedByMe = pagingDataItem.authorId == myId)
                }
            }
        }

    private val _feedState = MutableLiveData(StateFeed())
    val feedState: LiveData<StateFeed>
        get() = _feedState

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: SingleLiveEvent<Unit>
        get() = _postCreated

    private val _postEdited = SingleLiveEvent<Unit>()
    val postEdited: SingleLiveEvent<Unit>
        get() = _postEdited


    private val _postLoadError = SingleLiveEvent<String>()
    val postLoadError: SingleLiveEvent<String>
        get() = _postLoadError

    private val _postSaveError = SingleLiveEvent<String>()
    val postSaveError: SingleLiveEvent<String>
        get() = _postSaveError

    private val _photoState = MutableLiveData<PhotoModel?>()
    val photoState: LiveData<PhotoModel?>
        get() = _photoState

    private val _mentionedIdsState = MutableLiveData<Int>()
    val mentionedIdsState: LiveData<Int>
        get() = _mentionedIdsState


    private val edited = MutableLiveData(emptyPost)

    val postData: LiveData<ModelPost> = repository.postData.map(::ModelPost).asLiveData(Dispatchers.Default)
    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _feedState.value = StateFeed(loading = true)
            try {
                repository.getAllPosts()
                _feedState.value = StateFeed()
            } catch (e: Exception) {
                _feedState.value = StateFeed(error = true)
            }
        }
    }

    fun savePost() {
        edited.value?.let {
            postCreated.postValue(Unit)
            viewModelScope.launch {
                _feedState.value = StateFeed(loading = true)
                try {
                    _photoState.value?.let { photoModel ->
                        repository.savePostWithAttachment(it, photoModel.file)
                    } ?: run {
                        repository.savePost(it)
                    }
                    repository.savePost(it)
                    _feedState.value = StateFeed()
                } catch (e: Exception) {
                    _feedState.value = StateFeed(error = true)
                }
            }
        }
        edited.value = emptyPost
    }

    fun saveMentionedIds(list: List<Int>) {
        if (edited.value?.mentionIds == list) {
            return
        }
        edited.value =
            edited.value?.copy(mentionIds = list)
    }


    fun likePost(id: Long) {
        viewModelScope.launch {
            _feedState.value = StateFeed(loading = true)
            try {
                repository.like(id)
                _feedState.value = StateFeed()
            } catch (e: Exception) {
                _feedState.value = StateFeed(error = true)
            }
        }
    }

    fun unLikePost(id: Long) {
        viewModelScope.launch {
            _feedState.value = StateFeed(loading = true)
            try {
                repository.unLike(id)
                _feedState.value = StateFeed()
            } catch (e: Exception) {
                _feedState.value = StateFeed(error = true)
            }
        }
    }


    fun editPost(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value =
            edited.value?.copy(content = text)
    }

    fun removePost(id: Long) {
        viewModelScope.launch {
            _feedState.value = StateFeed(
                loading = true
            )
            try {
                repository.removePost(id)
                _feedState.value = StateFeed()
            } catch (e: Exception) {
                _feedState.value = StateFeed(error = true)
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