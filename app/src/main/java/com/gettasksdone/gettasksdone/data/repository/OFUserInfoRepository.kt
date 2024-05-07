package com.gettasksdone.gettasksdone.data.repository

import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.dao.UserInfoDao
import com.gettasksdone.gettasksdone.io.ApiService

class OFUserInfoRepository(
    private val userInfoDao: UserInfoDao,
    private val network: ApiService?,
    private val jwtHelper: JwtHelper
): UserInfoRepository(network, jwtHelper, userInfoDao) {
}