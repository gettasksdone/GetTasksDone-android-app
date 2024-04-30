package com.gettasksdone.gettasksdone

import android.app.Application
import com.gettasksdone.gettasksdone.data.local.AppDatabase
import com.gettasksdone.gettasksdone.data.repository.CheckItemRepository
import com.gettasksdone.gettasksdone.data.repository.ContextRepository
import com.gettasksdone.gettasksdone.data.repository.NoteRepository
import com.gettasksdone.gettasksdone.data.repository.ProjectRepository
import com.gettasksdone.gettasksdone.data.repository.TagRepository
import com.gettasksdone.gettasksdone.data.repository.TaskRepository
import com.gettasksdone.gettasksdone.data.repository.UserInfoRepository
import com.gettasksdone.gettasksdone.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val checkItemRepo by lazy { CheckItemRepository(database.checkItemDao()) }
    val contextRepo by lazy { ContextRepository(database.contextDao()) }
    val noteRepo by lazy { NoteRepository(database.noteDao()) }
    val projectRepo by lazy { ProjectRepository(database.projectDao()) }
    val tagRepo by lazy { TagRepository(database.tagDao()) }
    val taskRepo by lazy { TaskRepository(database.taskDao()) }
    val userInfoRepo by lazy { UserInfoRepository(database.userInfoDao()) }
    val userRepo by lazy { UserRepository(database.userDao()) }
}