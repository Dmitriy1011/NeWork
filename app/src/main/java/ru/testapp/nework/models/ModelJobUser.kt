package ru.testapp.nework.models

import ru.testapp.nework.dto.Job

data class ModelJobUser(
    val jobUserList: List<Job> = emptyList(),
    val empty: Boolean = false
)
