package ru.testapp.nework.state

data class StateFeed(
    val refreshing: Boolean = false,
    val error: Boolean = false,
    val loading: Boolean = false
)
