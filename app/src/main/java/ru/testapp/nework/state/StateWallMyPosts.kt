package ru.testapp.nework.state

data class StateWallMyPosts(
    val refreshing: Boolean = false,
    val loading: Boolean = false,
    val error: Boolean = false
)
