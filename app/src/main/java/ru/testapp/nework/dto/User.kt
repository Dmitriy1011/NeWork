package ru.testapp.nework.dto

import java.io.Serializable

data class User(
    val id: Long,
    val login: String,
    val name: String,
    val avatar: String?
)
