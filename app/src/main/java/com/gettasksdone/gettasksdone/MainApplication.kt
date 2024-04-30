package com.gettasksdone.gettasksdone

import android.app.Application
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val apiService: ApiService by lazy{ ApiService.create() }
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val checkItemRepo by lazy { OFCheckItemRepository(database.checkItemDao(), apiService) }
    val contextRepo by lazy { OFContextRepository(database.contextDao(), apiService) }
    val noteRepo by lazy { OFNoteRepository(database.noteDao(), apiService) }
    val projectRepo by lazy { OFProjectRepository(database.projectDao(), apiService) }
    val tagRepo by lazy { OFTagRepository(database.tagDao(), apiService) }
    val taskRepo by lazy { OFTaskRepository(database.taskDao(), apiService) }
    val userInfoRepo by lazy { OFUserInfoRepository(database.userInfoDao(), apiService) }
    val userRepo by lazy { OFUserRepository(database.userDao(), apiService) }
}