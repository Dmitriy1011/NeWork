package ru.testapp.nework.dto

import java.io.Serializable

data class TokenAuth(
    val id: Long,
    val token: String
)