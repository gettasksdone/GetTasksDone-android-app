package com.gettasksdone.gettasksdone.ui.ad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Algún Día Fragment"
    }
    val text: LiveData<String> = _text
}