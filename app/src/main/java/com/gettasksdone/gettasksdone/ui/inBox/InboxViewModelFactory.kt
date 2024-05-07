package com.gettasksdone.gettasksdone.ui.inBox

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.AppDatabase
import com.gettasksdone.gettasksdone.data.local.dao.TaskDao
import com.gettasksdone.gettasksdone.data.repository.TaskRepository
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.ui.ad.AdViewModel
import com.gettasksdone.gettasksdone.ui.agendado.AgendadoViewModel
import com.gettasksdone.gettasksdone.ui.esperando.EsperandoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext

class InboxViewModelFactory(
    private val jwtHelper: JwtHelper,
    private val context: Context,
    private val viewModelScope: CoroutineScope
) : ViewModelProvider.Factory {
        private val apiService: ApiService? by lazy{ ApiService.create() }
        private val database: AppDatabase by lazy { AppDatabase.getDatabase(context, viewModelScope) }
        private val taskRepo by lazy { TaskRepository(apiService, jwtHelper, database.taskDao()) }
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InboxViewModel::class.java)) {
                return InboxViewModel(jwtHelper, taskRepo) as T
            } else if (modelClass.isAssignableFrom(EsperandoViewModel::class.java)){
                return EsperandoViewModel(jwtHelper, taskRepo) as T
            } else if (modelClass.isAssignableFrom(AdViewModel::class.java)){
                return AdViewModel(jwtHelper, taskRepo) as T
            }   else if (modelClass.isAssignableFrom(AgendadoViewModel::class.java)) {
                return AgendadoViewModel(jwtHelper, taskRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }