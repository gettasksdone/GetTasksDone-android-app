package com.gettasksdone.gettasksdone.data.repository

import com.gettasksdone.gettasksdone.data.local.dao.ContextDao
import com.gettasksdone.gettasksdone.io.ApiService

class OFContextRepository(
    private val contextDao: ContextDao,
    private val network: ApiService
): ContextRepository(contextDao) {
}