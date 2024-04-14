package com.gettasksdone.gettasksdone.ui.agendado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AgendadoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Agendado Fragment"
    }
    val text: LiveData<String> = _text
}