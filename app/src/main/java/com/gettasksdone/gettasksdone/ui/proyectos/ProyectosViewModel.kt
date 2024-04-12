package com.gettasksdone.gettasksdone.ui.proyectos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProyectosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is proyectos Fragment"
    }
    val text: LiveData<String> = _text
}
