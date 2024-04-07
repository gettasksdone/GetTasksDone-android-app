package com.gettasksdone.gettasksdone.io.Requests

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String
)
