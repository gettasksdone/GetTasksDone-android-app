package com.gettasksdone.gettasksdone

import android.app.Application
import android.widget.Toast
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.AppDatabase
import com.gettasksdone.gettasksdone.data.repository.CheckItemRepository
import com.gettasksdone.gettasksdone.data.repository.ContextRepository
import com.gettasksdone.gettasksdone.data.repository.NoteRepository
import com.gettasksdone.gettasksdone.data.repository.OFCheckItemRepository
import com.gettasksdone.gettasksdone.data.repository.OFContextRepository
import com.gettasksdone.gettasksdone.data.repository.OFNoteRepository
import com.gettasksdone.gettasksdone.data.repository.OFProjectRepository
import com.gettasksdone.gettasksdone.data.repository.OFTagRepository
import com.gettasksdone.gettasksdone.data.repository.OFTaskRepository
import com.gettasksdone.gettasksdone.data.repository.OFUserInfoRepository
import com.gettasksdone.gettasksdone.data.repository.OFUserRepository
import com.gettasksdone.gettasksdone.data.repository.ProjectRepository
import com.gettasksdone.gettasksdone.data.repository.TagRepository
import com.gettasksdone.gettasksdone.data.repository.TaskRepository
import com.gettasksdone.gettasksdone.data.repository.UserInfoRepository
import com.gettasksdone.gettasksdone.data.repository.UserRepository
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.util.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val apiService: ApiService by lazy {
        val url = baseUrl()
        ApiService.setBaseUrl(url.toString())
        ApiService.create()
    }

    private fun baseUrl(): String? {
        val  preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
        val urlBase = preferencesTest.getString("urlBase", "")
        //Toast.makeText(applicationContext, "Debug server $urlBase", Toast.LENGTH_SHORT).show()
        return urlBase

    }

    private val jwtService = JwtHelper(this)
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }


    val checkItemRepo by lazy { OFCheckItemRepository(database.checkItemDao(), apiService, jwtService) }
    val contextRepo by lazy { OFContextRepository(database.contextDao(), apiService, jwtService) }
    val noteRepo by lazy { OFNoteRepository(database.noteDao(), apiService, jwtService) }
    val projectRepo by lazy { OFProjectRepository(database.projectDao(), apiService, jwtService) }
    val tagRepo by lazy { OFTagRepository(database.tagDao(), apiService, jwtService) }
    val taskRepo by lazy { OFTaskRepository(database.taskDao(), apiService, jwtService) }
    val userInfoRepo by lazy { OFUserInfoRepository(database.userInfoDao(), apiService, jwtService) }
    val userRepo by lazy { OFUserRepository(database.userDao(), apiService, jwtService) }
}