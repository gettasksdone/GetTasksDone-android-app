package com.gettasksdone.gettasksdone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.gettasksdone.gettasksdone.ui.Utils.AgregarUrlDialogFragment
import com.gettasksdone.gettasksdone.ui.Utils.NewContextDialogFragment
import com.gettasksdone.gettasksdone.util.PreferenceHelper


class MainActivity : AppCompatActivity(), AgregarUrlDialogFragment.NewUrlDialogListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val tvGoLogin = findViewById<ImageView>(R.id.startButton)
        tvGoLogin.setOnClickListener{
            goToLogin()
        }
    }
    private fun goToLogin(){
        val preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
        val urlList = preferencesTest.getStringSet("urlList", emptySet())
        if (urlList != null) {
            if(urlList.isEmpty()){
                Toast.makeText(applicationContext, "DEBUG: urlList empty", Toast.LENGTH_SHORT).show()
                openNewUrlActivity()
            }else{
                Toast.makeText(applicationContext, "DEBUG: urlList NOT empty", Toast.LENGTH_SHORT).show()
                val i = Intent(this, Login::class.java)
                startActivity(i)
            }
        }
    }

    override fun onDialogPositiveClick(newUrl: String) {
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

    private fun openNewUrlDialog() {
        val dialog = AgregarUrlDialogFragment()
        dialog.show(supportFragmentManager, "AgregarUrlDialog")
    }

    private fun openNewUrlActivity() {
        openNewUrlDialog()
    }
}