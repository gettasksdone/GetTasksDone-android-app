package com.gettasksdone.gettasksdone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.io.requests.RegisterRequest
import com.gettasksdone.gettasksdone.ui.Utils.AgregarUrlDialogFragment
import com.gettasksdone.gettasksdone.util.PreferenceHelper
import com.gettasksdone.gettasksdone.util.PreferenceHelper.get
import com.gettasksdone.gettasksdone.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class Registro : AppCompatActivity() , AgregarUrlDialogFragment.NewUrlDialogListener {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    private lateinit var serverSpinner: Spinner
    private lateinit var serverAdapter: ArrayAdapter<String>
    private var selectedUrl by Delegates.notNull<String>()
    private var urlList: MutableSet<String> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        val themePreferences = getSharedPreferences("MyPreferences", android.content.Context.MODE_PRIVATE)
        val whiteBackground = themePreferences.getBoolean("whiteBackground", false)

// Establece el tema correspondiente
        if (whiteBackground) {
            setTheme(R.style.Theme_MyApplication_WhiteBackground)
        } else {
            setTheme(R.style.Theme_MyApplication)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if(preferences["jwt",""].contains(".")) {
            //goToMenu()
        }

        val btnGoMenu=findViewById<Button>(R.id.btnCompletar)
        btnGoMenu.setOnClickListener{
            performRegister()
        }

        val btnGoLogin = findViewById<TextView>(R.id.goLogin)
        btnGoLogin.setOnClickListener {
            goToLogin()
        }

        //

        serverSpinner = findViewById(R.id.selector_servidor)
        serverAdapter = ArrayAdapter(this, R.layout.spinner_list, mutableListOf())
        serverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        serverSpinner.adapter = serverAdapter
        serverSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if(selectedItem == "Añadir un nuevo servidor"){
                    openNewUrlActivity()
                }else{
                    selectedUrl = urlList.elementAt(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "Se debe seleccionar un servidor", Toast.LENGTH_SHORT).show()
            }
        }

        loadUrls()
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
        preferences["jwt"] = jwt
    }

    private fun performRegister(){
        val etNombre = findViewById<EditText>(R.id.etUsername).text.toString()
        val etEmail = findViewById<EditText>(R.id.etCorreo).text.toString()
        val etPassword = findViewById<EditText>(R.id.etPassword).text.toString()
        val selectSpinner = findViewById<Spinner>(R.id.selector_servidor)
        val textSpinner = selectSpinner.selectedItem.toString()
        //Toast.makeText(applicationContext, "Debug server $textSpinner", Toast.LENGTH_SHORT).show()
        Log.w("Debug server", textSpinner)

        if(textSpinner == ""){
            Toast.makeText(applicationContext, "El campo Servidor es obligatorio", Toast.LENGTH_SHORT).show()
            return;
        }
        if(etNombre == ""){
            Toast.makeText(applicationContext, "El campo nombre de usuario es obligatorio", Toast.LENGTH_SHORT).show()
            return;
        }
        if(etPassword == ""){
            Toast.makeText(applicationContext, "El campo contraseña es obligatorio", Toast.LENGTH_SHORT).show()
            return;
        }

        if(etEmail == ""){
            Toast.makeText(applicationContext, "El campo email es obligatorio", Toast.LENGTH_SHORT).show()
            return;
        }

        try {
            ApiService.Factory.setBaseUrl(textSpinner)
        } catch (e: IllegalArgumentException) {
            throw  IllegalArgumentException("El formato de la URL no es válido")
        } catch (e: IllegalStateException) {
            throw  IllegalStateException("No se ha establecido la URL")
        }finally {
            //Toast.makeText(applicationContext, "DEBUG: url_base $textSpinner", Toast.LENGTH_SHORT).show()
        }

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
                    Log.e("API_RESPONSE", "Response from server: $registerResponse")
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

    override fun onDialogPositiveClick(newUrl: String) {
        if(newUrl.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "La URL del servidor no puede estar vacía",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if (!newUrl.startsWith("http://") &&!newUrl.startsWith("https://")) {
            Toast.makeText(
                applicationContext,
                "La URL del servidor debe empezar con http:// o https://",
                Toast.LENGTH_SHORT
            ).show()
            /*
        }else if(!isValidDomain(newUrl) && !isValidDomain(newUrl)){
            Toast.makeText(applicationContext, "La URL del servidor no es válida", Toast.LENGTH_SHORT).show()
        */
        }else {
            val preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
            val urlList = preferencesTest.getStringSet("urlList", emptySet())
            val newUrlList = mutableSetOf<String>()
            if (urlList != null) {
                newUrlList.addAll(urlList)
                with(preferencesTest.edit()) {
                    newUrlList.add(newUrl)
                    putStringSet("urlList", newUrlList)
                    apply()
                }
                Toast.makeText(
                    applicationContext,
                    "La URL del servidor ha sido agregada correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                loadUrls()
            }
        }
    }

    private fun openNewUrlDialog() {
        val dialog = AgregarUrlDialogFragment()
        dialog.show(supportFragmentManager, "AgregarUrlDialog")
    }

    private fun openNewUrlActivity() {
        openNewUrlDialog()
    }

    private fun loadUrls() {
        val preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
        val urlList = preferencesTest.getStringSet("urlList", emptySet())
        if(urlList != null){
            if(urlList.isNotEmpty()){
                Log.w("URLs", urlList.toString())
                this.urlList.clear()
                this.urlList = urlList
                this.urlList.add("Añadir un nuevo servidor")
                serverAdapter.clear()
                serverAdapter.addAll(this.urlList)
            }
        }
    }


}