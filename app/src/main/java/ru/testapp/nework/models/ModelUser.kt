package ru.testapp.nework.models

import ru.testapp.nework.dto.User

data class ModelUser(
    val users: List<User> = emptyList(),
    val empty: Boolean = false
)
