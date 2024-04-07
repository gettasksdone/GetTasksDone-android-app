package com.gettasksdone.gettasksdone.io.requests

data class CheckItemRequest (
    val content: String,
    val marked: Boolean
)