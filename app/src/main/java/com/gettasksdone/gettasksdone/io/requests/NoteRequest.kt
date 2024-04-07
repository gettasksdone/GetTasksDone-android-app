package com.gettasksdone.gettasksdone.io.requests

data class NoteRequest (
    val content: String,
    val creation: String = getCurrentDate(),
)