package com.gettasksdone.gettasksdone.ui.inBox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.ui.ad.AdViewModel
import com.gettasksdone.gettasksdone.ui.agendado.AgendadoViewModel
import com.gettasksdone.gettasksdone.ui.esperando.EsperandoViewModel

class InboxViewModelFactory(private val jwtHelper: JwtHelper) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InboxViewModel::class.java)) {
                return InboxViewModel(jwtHelper) as T
            } else if (modelClass.isAssignableFrom(EsperandoViewModel::class.java)){
                return EsperandoViewModel(jwtHelper) as T
            } else if (modelClass.isAssignableFrom(AdViewModel::class.java)){
                return AdViewModel(jwtHelper) as T
            }   else if (modelClass.isAssignableFrom(AgendadoViewModel::class.java)) {
                return AgendadoViewModel(jwtHelper) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }