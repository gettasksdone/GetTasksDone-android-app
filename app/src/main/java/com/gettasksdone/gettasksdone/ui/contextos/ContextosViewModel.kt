package com.gettasksdone.gettasksdone.ui.contextos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.data.repository.ContextRepository
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.io.requests.ContextRequest
import com.gettasksdone.gettasksdone.model.Context
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ContextosViewModel(
    private val jwtHelper: JwtHelper,
    private val repository: ContextRepository
) : ViewModel() {

    private val apiService: ApiService? by lazy{
        ApiService.create()
    }

    private val _contextList = MutableLiveData<List<Context>>()
    val contextList: MutableLiveData<List<Context>> = _contextList

    init {
        loadContexts()
    }

    fun loadContexts() {
        viewModelScope.launch {
            _contextList.value = repository.getAll()
            Log.d("ContextosViewModel", "Contextos: ${_contextList.value}")
        }
    }
    fun deleteContext(contextId: Long) { //TODO: Revisar lógica de borrado con offline
        // Aquí puedes hacer la llamada a la API para eliminar el contexto
        // Asegúrate de manejar correctamente las respuestas de la API y los posibles errores
        val authHeader = "Bearer ${jwtHelper.getToken()}"
        apiService?.deleteContext(contextId, authHeader)?.enqueue(object : Callback<String> {
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
    fun updateContextName(contextId: Long, newName: String) { //TODO: Revisar lógica de actualización con offline
        val authHeader = "Bearer ${jwtHelper.getToken()}"
        val request = ContextRequest(newName)  // Asume que ContextRequest es una clase que representa la solicitud de actualización
        apiService?.updateContext(contextId, authHeader, request)?.enqueue(object : Callback<String> {
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

