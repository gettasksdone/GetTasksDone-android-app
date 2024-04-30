package com.gettasksdone.gettasksdone.data.repository

import com.gettasksdone.gettasksdone.data.local.dao.TagDao
import com.gettasksdone.gettasksdone.io.ApiService

class OFTagRepository(
    private val tagDao: TagDao,
    private val network: ApiService
): TagRepository(tagDao) {
}