package com.gettasksdone.gettasksdone.data.repository

import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.dao.CheckItemDao
import com.gettasksdone.gettasksdone.io.ApiService

class OFCheckItemRepository(
    private val checkItemDao: CheckItemDao,
    private val network: ApiService?,
    private val jwtHelper: JwtHelper
): CheckItemRepository(network, jwtHelper, checkItemDao) {
}