package com.gettasksdone.gettasksdone.ui.proyectos

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.AppDatabase
import com.gettasksdone.gettasksdone.data.repository.ProjectRepository
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.ui.inBox.InboxViewModel
import kotlinx.coroutines.CoroutineScope

class ProjectViewModelFactory(
    private val jwtHelper: JwtHelper,
    private val context: Context,
    private val viewModelScope: CoroutineScope
): ViewModelProvider.Factory {
    private val apiService: ApiService? by lazy { ApiService.create() }
    private val database: AppDatabase by lazy { AppDatabase.getDatabase(context, viewModelScope) }
    private val projectRepo by lazy { ProjectRepository(apiService, jwtHelper, database.projectDao()) }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProyectosViewModel::class.java)) {
            return ProyectosViewModel(jwtHelper, projectRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}