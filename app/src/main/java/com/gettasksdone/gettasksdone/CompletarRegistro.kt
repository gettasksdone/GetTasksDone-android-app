package com.gettasksdone.gettasksdone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.io.requests.UserDataRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompletarRegistro : AppCompatActivity() {

    private val apiService : ApiService? by lazy {
        ApiService.create()
    }

    private val jwtHelper: JwtHelper by lazy {
        JwtHelper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        val preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val whiteBackground = preferences.getBoolean("whiteBackground", false)

        // Establece el tema correspondiente
        if (whiteBackground) {
            setTheme(R.style.Theme_MyApplication_WhiteBackground)
        } else {
            setTheme(R.style.Theme_MyApplication)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completar_registro2)

        val nombre = intent.getStringExtra("nombre")
        val telefono = intent.getStringExtra("telefono")
        val departamento = intent.getStringExtra("departamento")
        val puesto = intent.getStringExtra("puesto")

        findViewById<EditText>(R.id.etNombre).setText(nombre)
        findViewById<EditText>(R.id.etTelefono).setText(telefono)
        findViewById<EditText>(R.id.etDepartamento).setText(departamento)
        findViewById<EditText>(R.id.etPuesto).setText(puesto)

        val btnGoMenu = findViewById<Button>(R.id.btnCompletar)
        btnGoMenu.setOnClickListener{
            performCompleteRegister()
        }
    }

    private fun goToMenu(){
        val i = Intent(this, Menu::class.java)
        startActivity(i)
        finish()
    }

    private fun performCompleteRegister(){
        val etNombre = findViewById<EditText>(R.id.etNombre).text.toString()
        val etTelefono = findViewById<EditText>(R.id.etTelefono).text.toString()
        val etDpto = findViewById<EditText>(R.id.etDepartamento).text.toString()
        val etPuesto = findViewById<EditText>(R.id.etPuesto).text.toString()

        //jwtHelper.getId()

        val registerRequest = UserDataRequest(
            nombre = etNombre,
            telefono = etTelefono,
            departamento = etDpto,
            puesto = etPuesto
        )

        val authHeader = "Bearer ${jwtHelper.getToken()}"

        val call = apiService?.completeRegister(authHeader, registerRequest)
        if (call != null) {
            call.enqueue(object : Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val registerResponse = response.body()
                    if(response.isSuccessful) {
                        if (registerResponse == null) {
                            Toast.makeText(
                                applicationContext,
                                "Se produjo un error en el servidor (onResponse)",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        Toast.makeText(applicationContext, "Informacion actualizada correctamente", Toast.LENGTH_SHORT).show()
                        goToMenu()

                    } else {
                        // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                        Toast.makeText(applicationContext, "Error actualizando datos de usuario", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("API_CALL", "Error en onFailure(): ${t.message}")

                    Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()

                }

            })
        }

    }
}