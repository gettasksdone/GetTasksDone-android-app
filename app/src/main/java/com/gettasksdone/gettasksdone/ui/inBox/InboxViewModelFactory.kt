package com.gettasksdone.gettasksdone.ui.inBox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.data.JwtHelper

class InboxViewModelFactory(private val jwtHelper: JwtHelper) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InboxViewModel::class.java)) {
                return InboxViewModel(jwtHelper) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }