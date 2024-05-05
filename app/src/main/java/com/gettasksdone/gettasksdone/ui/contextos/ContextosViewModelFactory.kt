package com.gettasksdone.gettasksdone.ui.contextos

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.local.AppDatabase
import com.gettasksdone.gettasksdone.data.repository.ContextRepository
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.ui.ad.AdViewModel
import com.gettasksdone.gettasksdone.ui.agendado.AgendadoViewModel
import com.gettasksdone.gettasksdone.ui.esperando.EsperandoViewModel
import kotlinx.coroutines.CoroutineScope

class ContextosViewModelFactory(
    private val jwtHelper: JwtHelper,
    private val context: Context,
    private val viewModelScope: CoroutineScope
) : ViewModelProvider.Factory {
    private val apiService: ApiService by lazy { ApiService.create() }
    private val database: AppDatabase by lazy { AppDatabase.getDatabase(context, viewModelScope) }
    private val contextRepo by lazy { ContextRepository(apiService, jwtHelper, database.contextDao()) }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContextosViewModel::class.java)) {
            return ContextosViewModel(jwtHelper, contextRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}