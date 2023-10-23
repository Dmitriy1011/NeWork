package ru.testapp.nework.models

import ru.testapp.nework.dto.Post

data class ModelPost(
    val posts: List<Post> = emptyList()
)
