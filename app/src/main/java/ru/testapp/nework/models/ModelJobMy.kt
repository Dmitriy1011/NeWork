package ru.testapp.nework.models

import ru.testapp.nework.dto.Job

data class ModelJobMy(
    val jobsMy: List<Job> = emptyList(),
    val empty: Boolean = false
)
