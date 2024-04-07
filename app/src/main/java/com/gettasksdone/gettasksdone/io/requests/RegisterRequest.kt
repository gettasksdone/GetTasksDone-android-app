package com.gettasksdone.gettasksdone.io.requests

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String
)
