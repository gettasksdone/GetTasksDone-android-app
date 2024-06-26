package com.gettasksdone.gettasksdone.data.repository

import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.dao.ProjectDao
import com.gettasksdone.gettasksdone.io.ApiService

class OFProjectRepository(
    private val projectDao: ProjectDao,
    private val network: ApiService?,
    private val jwtHelper: JwtHelper
): ProjectRepository(network, jwtHelper, projectDao) {
}