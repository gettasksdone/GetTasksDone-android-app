package com.gettasksdone.gettasksdone.data.repository

import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.dao.UserDao
import com.gettasksdone.gettasksdone.io.ApiService

class OFUserRepository(
    private val userDao: UserDao,
    private val network: ApiService,
    private val jwtHelper: JwtHelper
): UserRepository(network, jwtHelper, userDao) {
}