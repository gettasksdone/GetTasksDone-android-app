package com.gettasksdone.gettasksdone.ui.esperando

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EsperandoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is esperando Fragment"
    }
    val text: LiveData<String> = _text
}