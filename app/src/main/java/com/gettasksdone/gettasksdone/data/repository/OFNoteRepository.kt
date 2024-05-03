package com.gettasksdone.gettasksdone.data.repository

import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.dao.NoteDao
import com.gettasksdone.gettasksdone.io.ApiService

class OFNoteRepository(
    private val noteDao: NoteDao,
    private val network: ApiService,
    private val jwtHelper: JwtHelper
): NoteRepository(network, jwtHelper, noteDao) {
}