package com.gettasksdone.gettasksdone.data.repository

import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.dao.TaskDao
import com.gettasksdone.gettasksdone.io.ApiService

class OFTaskRepository(
    private val taskDao: TaskDao,
    private val network: ApiService?,
    private val jwtHelper: JwtHelper
): TaskRepository(network, jwtHelper, taskDao) {
}