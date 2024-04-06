package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.io.ApiService
import com.example.myapplication.io.RegisterRequest
import com.example.myapplication.util.PreferenceHelper
import com.example.myapplication.util.PreferenceHelper.get
import com.example.myapplication.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if(preferences["jwt",""].contains(".")) {
            //goToMenu()
        }

        val btnGoMenu=findViewById<Button>(R.id.button2)
        btnGoMenu.setOnClickListener{
            performRegister()
        }

        val btnGoLogin = findViewById<TextView>(R.id.goLogin)
        btnGoLogin.setOnClickListener {
            goToLogin()
        }
    }

    private fun goToLogin(){
        val i = Intent(this, Login::class.java)
        startActivity(i)
        finish()
    }

    private fun goToCompleteRegister(){
        val i = Intent(this, CompletarRegistro::class.java)
        //createSessionPreference()
        startActivity(i)
        finish()
    }

    private fun createSessionPreference(jwt: String){
        val preferences=PreferenceHelper.defaultPrefs(this)
        preferences ["jwt"]=jwt;
    }

    private fun performRegister(){
        val etNombre = findViewById<EditText>(R.id.etNombre).text.toString()
        val etEmail = findViewById<EditText>(R.id.etTelefono).text.toString()
        val etPassword = findViewById<EditText>(R.id.etDepartamento).text.toString()

        val registerRequest = RegisterRequest(username = etNombre, email = etEmail, password = etPassword)
        //Log.d("username:", "$etEmail")
        //Log.d("password:", "$etPassword")

        val call = apiService.postRegister(registerRequest)
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
                    Toast.makeText(applicationContext, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                    createSessionPreference(registerResponse)
                    goToCompleteRegister()

                } else {
                    // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                    Toast.makeText(applicationContext, "Error creando el usuario", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API_CALL", "Error en onFailure(): ${t.message}")

                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()

            }

        })

    }
}