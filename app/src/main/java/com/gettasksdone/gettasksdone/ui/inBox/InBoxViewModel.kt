package com.gettasksdone.gettasksdone.ui.inBox

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Task
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import java.io.IOException
class InboxViewModel(private val jwtHelper: JwtHelper) : ViewModel() {

    private val apiService: ApiService by lazy{
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
            try {
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                // Realizar la llamada a la API para obtener las tareas
                val call = apiService.getTasks(authHeader)
                call.enqueue(object : Callback<List<Task>> {
                    override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                        if (response.isSuccessful) {
                            val tasksFromApi = response.body()
                            // Actualizar el LiveData con las tareas obtenidas
                            _tasks.value = tasksFromApi ?: _tasks.value
                        } else {

                        }
                    }

                    override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                        // Manejar errores de llamada a la API
                        // Puedes mostrar un mensaje de error en la interfaz de usuario o manejarlo de otra manera
                        t.printStackTrace()
                    }
                })
            } catch (e: IOException) {
                // Manejar errores de red
                e.printStackTrace()
            } catch (e: Throwable) {
                // Manejar otros tipos de excepciones
                e.printStackTrace()
            }
        }
    }

}