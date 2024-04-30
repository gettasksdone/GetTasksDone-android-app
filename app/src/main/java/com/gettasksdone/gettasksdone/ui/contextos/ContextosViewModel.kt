package com.gettasksdone.gettasksdone.ui.contextos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.io.requests.ContextRequest
import com.gettasksdone.gettasksdone.model.Context
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ContextosViewModel(private val jwtHelper: JwtHelper) : ViewModel() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    private val _contextList = MutableLiveData<List<Context>>()
    val contextList: MutableLiveData<List<Context>> = _contextList

    init {
        loadContexts()
    }

    fun loadContexts() {
        viewModelScope.launch {
            try {
                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = apiService.getContexts(authHeader)
                call.enqueue(object : Callback<List<Context>> {
                    override fun onResponse(call: Call<List<Context>>, response: Response<List<Context>>) {
                        if (response.isSuccessful) {
                            val contextsFromApi = response.body()
                            _contextList.value = contextsFromApi ?: emptyList()
                        } else {
                            // Maneja el caso en que la respuesta HTTP no es exitosa
                        }
                    }

                    override fun onFailure(call: Call<List<Context>>, t: Throwable) {
                        // Maneja el caso en que la llamada a la API falla
                    }
                })
            } catch (e: IOException) {
                // Maneja errores de red
            } catch (e: Throwable) {
                // Maneja otros tipos de excepciones
            }
        }
    }
    fun deleteContext(contextId: Long) {
        // Aquí puedes hacer la llamada a la API para eliminar el contexto
        // Asegúrate de manejar correctamente las respuestas de la API y los posibles errores
        val authHeader = "Bearer ${jwtHelper.getToken()}"
        apiService.deleteContext(contextId, authHeader).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, actualiza tu lista de contextos
                    loadContexts()
                } else {
                    // Maneja el caso en que la respuesta HTTP no es exitosa
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // Maneja el caso en que la llamada a la API falla
            }
        })
    }
    fun updateContextName(contextId: Long, newName: String) {
        val authHeader = "Bearer ${jwtHelper.getToken()}"
        val request = ContextRequest(newName)  // Asume que ContextRequest es una clase que representa la solicitud de actualización
        apiService.updateContext(contextId, authHeader, request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, actualiza tu lista de contextos
                    loadContexts()
                } else {
                    // Maneja el caso en que la respuesta HTTP no es exitosa
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // Maneja el caso en que la llamada a la API falla
            }
        })
    }
}

