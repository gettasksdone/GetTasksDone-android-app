package com.gettasksdone.gettasksdone

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.io.requests.ProjectRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CreateProject : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val jwtHelper: JwtHelper by lazy {
        JwtHelper(this)
    }

    private lateinit var spinner1: Spinner
    private lateinit var adapter1: ArrayAdapter<String>
    private lateinit var selectedState: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_project)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val estados = mutableListOf("empezar", "esperando", "algún día")
        spinner1 = findViewById(R.id.estadoProyecto)
        adapter1 = ArrayAdapter(this, R.layout.spinner_list, estados)
        spinner1.adapter = adapter1
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                selectedState = selectedItem
                //no se pueden crear estados nuevos
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar el caso en que no se seleccione nada
            }
        }

        val btnCrear = findViewById<Button>(R.id.botonCrearProyecto)
        btnCrear.setOnClickListener{
            performCreateProject()
        }
    }

    fun showDatePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth+1}/$selectedYear"
            (view as EditText).setText(selectedDate)
        }, year, month, day).show()
    }
    private fun goToMenu(){
        val i = Intent(this, Menu::class.java)
        startActivity(i)
        finish()
    }

    private fun performCreateProject(){

        val createProjectRequest = ProjectRequest(
            nombre = findViewById<EditText>(R.id.nombreProyecto).text.toString(),
            inicio = findViewById<EditText>(R.id.inicioProyecto).text.toString(),
            fin = findViewById<EditText>(R.id.finProyecto).text.toString(),
            descripcion = findViewById<EditText>(R.id.descripcionProyecto).text.toString(),
            estado = selectedState
        )

        val authHeader = "Bearer ${jwtHelper.getToken()}"
        val call = apiService.createProject(authHeader, createProjectRequest)

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
                    Toast.makeText(applicationContext, "Proyecto creado correctamente", Toast.LENGTH_SHORT).show()
                    goToMenu()

                } else {
                    // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                    Toast.makeText(applicationContext, "Error al crear el proyecto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API_CALL", "Error en onFailure(): ${t.message}")

                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()

            }
        })
    }
}

