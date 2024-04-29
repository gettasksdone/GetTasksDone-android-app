package com.gettasksdone.gettasksdone.ui.contextos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Context
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ContextosViewModel(private val apiService: ApiService, private val jwtHelper: JwtHelper) : ViewModel() {

    private val _contextList = MutableLiveData<List<Context>?>()
    val contextList: MutableLiveData<List<Context>?> = _contextList

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
                            val contextResponse = response.body()
                            if (contextResponse != null) {
                                _contextList.value = contextResponse
                            } else {
                                _contextList.value = emptyList()
                            }
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
}

