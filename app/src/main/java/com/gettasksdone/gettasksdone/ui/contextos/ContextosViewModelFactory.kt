package com.gettasksdone.gettasksdone.ui.contextos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.ui.ad.AdViewModel
import com.gettasksdone.gettasksdone.ui.agendado.AgendadoViewModel
import com.gettasksdone.gettasksdone.ui.esperando.EsperandoViewModel

class ContextosViewModelFactory(private val jwtHelper: JwtHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContextosViewModel::class.java)) {
            return ContextosViewModel(jwtHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}