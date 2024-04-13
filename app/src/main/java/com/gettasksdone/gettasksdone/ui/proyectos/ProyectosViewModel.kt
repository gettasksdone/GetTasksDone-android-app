package com.gettasksdone.gettasksdone.ui.proyectos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Project
import com.gettasksdone.gettasksdone.model.Task
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ProyectosViewModel(private val jwtHelper: JwtHelper) : ViewModel() {

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
            try {
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                // Realizar la llamada a la API para obtener las tareas
                val call = apiService.getProjects(authHeader)
                call.enqueue(object : Callback<List<Project>> {
                    override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                        if (response.isSuccessful) {
                            val projectsFromApi = response.body()
                            // Actualizar el LiveData con las tareas obtenidas
                            _projects.value = projectsFromApi ?: _projects.value
                        } else {

                        }
                    }

                    override fun onFailure(call: Call<List<Project>>, t: Throwable) {
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
