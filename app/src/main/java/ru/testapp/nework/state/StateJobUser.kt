package ru.testapp.nework.state

data class StateJobUser(
    val refreshing: Boolean = false,
    val loading: Boolean = false,
    val error: Boolean = false
)

