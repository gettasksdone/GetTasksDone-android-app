package com.gettasksdone.gettasksdone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.io.Requests.LoginRequest
import com.gettasksdone.gettasksdone.util.PreferenceHelper
import com.gettasksdone.gettasksdone.util.PreferenceHelper.get
import com.gettasksdone.gettasksdone.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {


    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if(preferences["jwt",""].contains(".")) {
            //goToMenu()
        }
           // Toast.makeText(applicationContext, "prueba",Toast.LENGTH_SHORT).show()


        val btnGoMenu=findViewById<Button>(R.id.button)
        btnGoMenu.setOnClickListener{
            performLogin()
        }

        val btnGoRegister = findViewById<TextView>(R.id.register)
        btnGoRegister.setOnClickListener{
            goToRegister()
        }
    }

    private fun goToRegister(){
        val i = Intent(this, Registro::class.java)
        startActivity(i)
        finish()
    }

    private fun goToMenu(){
        val i = Intent(this, Menu::class.java)
        //createSessionPreference()
        startActivity(i)
        finish()
    }

    private fun createSessionPreference(jwt: String){
        val preferences=PreferenceHelper.defaultPrefs(this)
        preferences ["jwt"]=jwt;
    }

    private fun performLogin(){
        val etEmail = findViewById<EditText>(R.id.et_email).text.toString()
        val etPassword = findViewById<EditText>(R.id.et_password).text.toString()

        val loginRequest = LoginRequest(username = etEmail, password = etPassword)
        //Log.d("username:", "$etEmail")
        //Log.d("password:", "$etPassword")

        val call = apiService.postLogin(loginRequest)
        call.enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val loginResponse = response.body()
                if(response.isSuccessful) {
                    if (loginResponse == null) {
                        Toast.makeText(
                            applicationContext,
                            "Se produjo un error en el servidor (onResponse)",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    Log.e("API_RESPONSE", "Response from server: $loginResponse")
                    createSessionPreference(loginResponse)
                    goToMenu()

                } else {
                    // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                    Toast.makeText(applicationContext, "Usuario o contraseña no válido", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API_CALL", "Error en onFailure(): ${t.message}")

                Toast.makeText(applicationContext, "Se produjo un error en el servidor (onFailure)", Toast.LENGTH_SHORT).show()

            }

        })

    }
}
