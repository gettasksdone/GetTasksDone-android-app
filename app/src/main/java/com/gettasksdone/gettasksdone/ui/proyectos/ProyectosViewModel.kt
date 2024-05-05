package com.gettasksdone.gettasksdone.ui.proyectos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.repository.ProjectRepository
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Project
import com.gettasksdone.gettasksdone.model.Task
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ProyectosViewModel(
    private val jwtHelper: JwtHelper,
    private val repository: ProjectRepository
) : ViewModel() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> get() = _projects

    init{
        getProjects()
    }

    fun getProjects(){
        viewModelScope.launch {
            _projects.value = repository.getAll()
            Log.d("ProyectosViewModel", "Proyectos: ${_projects.value}")
        }
    }
}
