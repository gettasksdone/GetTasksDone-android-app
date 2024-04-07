package com.gettasksdone.gettasksdone.io.requests

data class ProjectRequest (
    val name: String,
    val start: String = getCurrentDate(),
    val finish: String,
    val description: String,
    val status: String
)