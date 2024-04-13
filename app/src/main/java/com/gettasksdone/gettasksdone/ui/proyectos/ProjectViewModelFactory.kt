package com.gettasksdone.gettasksdone.ui.proyectos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.ui.inBox.InboxViewModel

class ProjectViewModelFactory(private val jwtHelper: JwtHelper): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProyectosViewModel::class.java)) {
            return ProyectosViewModel(jwtHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}