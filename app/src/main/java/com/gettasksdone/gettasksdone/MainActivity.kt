package com.gettasksdone.gettasksdone

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.ui.Utils.AgregarUrlDialogFragment
import com.gettasksdone.gettasksdone.ui.Utils.NewContextDialogFragment
import com.gettasksdone.gettasksdone.util.PreferenceHelper
import com.gettasksdone.gettasksdone.util.PreferenceHelper.get


class MainActivity : AppCompatActivity(), AgregarUrlDialogFragment.NewUrlDialogListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Comprueba las preferencias compartidas para ver si el fondo debe ser blanco
        val preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val whiteBackground = preferences.getBoolean("whiteBackground", false)
        // Establece el tema correspondiente
        if (whiteBackground) {
            setTheme(R.style.Theme_MyApplication_WhiteBackground)
        } else {
            setTheme(R.style.Theme_MyApplication)
        }
        // Configura las comprobaciones de conexión del dispositivo
        val networkManager = getSystemService(ConnectivityManager::class.java)
        val offlineDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        offlineDialogBuilder
            .setTitle("No hay conexión a internet")
            .setMessage("La aplicación se ejecutará en modo de solo lectura. Para activar las funciones de escritura, conecte el dispositivo a internet y reinicie la aplicación.")
            .setPositiveButton("Aceptar") { _, _ ->
                val i = Intent(this, Menu::class.java)
                startActivity(i)
                finish()
            }
        val offlineDialog = offlineDialogBuilder.create()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvGoLogin = findViewById<ImageView>(R.id.startButton)
        tvGoLogin.setOnClickListener{
            //Si no tiene conexión a internet, le iniciará la aplicación en solo lectura
            val networkInfo = networkManager.activeNetwork
            if(networkInfo == null){
                offlineDialog.show()
            }else{
                goToLogin()
            }
        }
        val myButton = findViewById<Button>(R.id.myButton)
        myButton.setOnClickListener {
            // Cambia el estado del fondo en las preferencias compartidas
            val editor = preferences.edit()
            editor.putBoolean("whiteBackground", !whiteBackground)
            editor.apply()

            // Recrea la actividad para aplicar el nuevo tema
            recreate()
        }
    }
    private fun goToLogin(){
        val preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
        val urlList = preferencesTest.getStringSet("urlList", emptySet())

        if (urlList != null) {
            if(urlList.isEmpty()){
                //Toast.makeText(applicationContext, "DEBUG: urlList empty ${urlList.toString()}", Toast.LENGTH_SHORT).show()
                openNewUrlActivity()
            }else{
                //Toast.makeText(applicationContext, "DEBUG: urlList NOT empty ${urlList.toString()}" , Toast.LENGTH_SHORT).show()
                val i = Intent(this, Login::class.java)
                startActivity(i)
            }
        }
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
        }
        else{
            val preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
            val urlList = preferencesTest.getStringSet("urlList", emptySet())
            val newUrlList = mutableSetOf<String>()
            if(urlList != null){
                newUrlList.addAll(urlList)
                with(preferencesTest.edit()){
                    newUrlList.add(newUrl)
                    putStringSet("urlList", newUrlList)
                    apply()
                }

                Toast.makeText(applicationContext, "La URL del servidor ha sido agregada correctamente", Toast.LENGTH_SHORT).show()
            }
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }

    }

    private fun openNewUrlDialog() {
        val dialog = AgregarUrlDialogFragment()
        dialog.show(supportFragmentManager, "AgregarUrlDialog")
    }

    private fun openNewUrlActivity() {
        openNewUrlDialog()
    }

    fun isValidDomain(url: String): Boolean {
        val domainNameRegex = Regex(
            "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$"
        )
        val uri = Uri.parse(url)
        val host = uri.host ?: return false
        return domainNameRegex.matches(host)
    }

    fun isValidIP(url: String): Boolean {
        val uri = Uri.parse(url)
        val host = uri.host ?: return false
        return Patterns.IP_ADDRESS.matcher(host).matches()
    }
}