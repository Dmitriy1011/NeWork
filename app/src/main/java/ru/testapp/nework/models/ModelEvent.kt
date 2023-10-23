package ru.testapp.nework.models

import ru.testapp.nework.dto.Event

data class ModelEvent(
    val eventsList: List<Event> = emptyList()
)