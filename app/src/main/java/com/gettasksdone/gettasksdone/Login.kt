package com.gettasksdone.gettasksdone

import android.content.ContentValues.TAG
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
import com.gettasksdone.gettasksdone.io.requests.LoginRequest
import com.gettasksdone.gettasksdone.ui.Utils.AgregarUrlDialogFragment
import com.gettasksdone.gettasksdone.util.PreferenceHelper
import com.gettasksdone.gettasksdone.util.PreferenceHelper.get
import com.gettasksdone.gettasksdone.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlin.properties.Delegates

class Login : AppCompatActivity(), AgregarUrlDialogFragment.NewUrlDialogListener {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

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
        setContentView(R.layout.activity_login2)
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
        // Configura las opciones de inicio de sesión de Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Crea un cliente de GoogleSignIn con las opciones especificadas por gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if(preferences["jwt",""].contains(".")) {
            //goToMenu()
        }

        val btnGoMenu=findViewById<Button>(R.id.button)
        btnGoMenu.setOnClickListener{
            performLogin()
        }

        val btnGoRegister = findViewById<TextView>(R.id.register)
        btnGoRegister.setOnClickListener{
            goToRegister()
        }
        // Establece un OnClickListener en el botón de inicio de sesión de Google
        findViewById<com.google.android.gms.common.SignInButton>(R.id.sign_in_button).setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

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
                this.urlList = this.urlList.sortedDescending().toMutableSet()
                serverAdapter.clear()
                serverAdapter.addAll(this.urlList)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Resultado devuelto desde el lanzamiento del Intent desde GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                handleSignInResult(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun handleSignInResult(account: GoogleSignInAccount) {
        // Aquí puedes manejar el resultado del inicio de sesión
        // 'account' contiene la información de la cuenta de Google del usuario
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
        preferences ["jwt"]=jwt
    }

    private fun performLogin(){
        val etUsername = findViewById<EditText>(R.id.et_username).text.toString()
        val etPassword = findViewById<EditText>(R.id.et_password).text.toString()
        val selectSpinner = findViewById<Spinner>(R.id.selector_servidor)
        val textSpinner = selectSpinner.selectedItem.toString()
        //Toast.makeText(applicationContext, "Debug server $textSpinner", Toast.LENGTH_SHORT).show()
        Log.w("Debug server", textSpinner)


        if (textSpinner == ""){
            Toast.makeText(applicationContext, "El campo Servidor es obligatorio", Toast.LENGTH_SHORT).show()
            return;
        }

        if(etUsername == ""){
            Toast.makeText(applicationContext, "El campo nombre de usuario es obligatorio", Toast.LENGTH_SHORT).show()
            return;
        }
        if(etPassword == ""){
            Toast.makeText(applicationContext, "El campo contraseña es obligatorio", Toast.LENGTH_SHORT).show()
            return;
        }

        //Añadiendo la clave urlBase al PreferenceHelper

        val preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
        with(preferencesTest.edit()){
            putString("urlBase", textSpinner)
            apply()
        }



        ApiService.Factory.setBaseUrl(textSpinner)


        //Toast.makeText(applicationContext, "Debug server bien $textSpinner", Toast.LENGTH_SHORT).show()


        try {
            ApiService.Factory.setBaseUrl(textSpinner)
        } catch (e: IllegalArgumentException) {
            throw  IllegalArgumentException("El formato de la URL no es válido")
        } catch (e: IllegalStateException) {
            throw  IllegalStateException("No se ha establecido la URL")
        }finally {
            //Toast.makeText(applicationContext, "DEBUG: url_base $textSpinner", Toast.LENGTH_SHORT).show()
        }



        val loginRequest = LoginRequest(username = etUsername, password = etPassword)
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
}
