package com.gettasksdone.gettasksdone.ui.ad

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.repository.TaskRepository
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Task
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AdViewModel(
    private val jwtHelper: JwtHelper,
    private val repository: TaskRepository
) : ViewModel() {

    private val apiService: ApiService? by lazy{
        ApiService.create()
    }

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    init {
        // Cuando se inicializa el ViewModel, llama a la API para obtener las tareas
        getTasks()
    }

    fun getTasks() {
        viewModelScope.launch {
            val tasks = repository.getAll()
            val filteredTasks = tasks.filter { task ->
                task.estado == "algún día"
            }
            _tasks.value = filteredTasks
            Log.d("TASKS:", _tasks.toString())
        }
    }


}