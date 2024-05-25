package com.gettasksdone.gettasksdone

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.io.requests.ProjectRequest
import com.gettasksdone.gettasksdone.util.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateProject : AppCompatActivity() {

    private val apiService: ApiService? by lazy {
        val url = baseUrl()
        ApiService.setBaseUrl(url.toString())
        ApiService.create()
    }

    private fun baseUrl(): String? {
        val  preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
        val urlBase = preferencesTest.getString("urlBase", "")
        //Toast.makeText(applicationContext, "Debug server $urlBase", Toast.LENGTH_SHORT).show()
        return urlBase

    }

    private val jwtHelper: JwtHelper by lazy {
        JwtHelper(this)
    }

    private lateinit var spinner1: Spinner
    private lateinit var adapter1: ArrayAdapter<String>
    private lateinit var selectedState: String

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val whiteBackground = preferences.getBoolean("whiteBackground", false)

        // Establece el tema correspondiente
        if (whiteBackground) {
            setTheme(R.style.Theme_MyApplication_WhiteBackground)
        } else {
            setTheme(R.style.Theme_MyApplication)
        }

        setContentView(R.layout.activity_create_project)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtiene el color de texto del tema actual
        val attrs = intArrayOf(android.R.attr.textColor) // Atributo definido en tu tema
        val typedArray = theme.obtainStyledAttributes(attrs)
        val defaultColor = Color.BLACK  // Usa un color por defecto si el atributo no está definido
        val textColor = typedArray.getColor(0, defaultColor)
        typedArray.recycle()

        // Establece el color del texto y del texto de sugerencia de los campos de texto al color de texto del tema
        val etNombreProyecto = findViewById<EditText>(R.id.nombreProyecto)
        etNombreProyecto.setTextColor(textColor)
        etNombreProyecto.setHintTextColor(textColor)

        val etInicioProyecto = findViewById<EditText>(R.id.inicioProyecto)
        etInicioProyecto.setTextColor(textColor)
        etInicioProyecto.setHintTextColor(textColor)

        val etFinProyecto = findViewById<EditText>(R.id.finProyecto)
        etFinProyecto.setTextColor(textColor)
        etFinProyecto.setHintTextColor(textColor)

        val etDescripcionProyecto = findViewById<EditText>(R.id.descripcionProyecto)
        etDescripcionProyecto.setTextColor(textColor)
        etDescripcionProyecto.setHintTextColor(textColor)
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_create_project)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //modificar el numero maximo de lineas de la descripcion para que funcione mejor el teclado
        val descripcion = findViewById<EditText>(R.id.descripcionProyecto)
        descripcion.maxLines = 2;
        descripcion.setImeOptions(EditorInfo.IME_ACTION_DONE);
        descripcion.setRawInputType(InputType.TYPE_CLASS_TEXT);

        val estados = mutableListOf("empezar", "esperando", "algún día")
        spinner1 = findViewById(R.id.estadoProyecto)
        adapter1 = ArrayAdapter(this,R.layout.spinnercolor, estados)
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

        if (intent.getBooleanExtra("editMode", false)) {
            val windowTitle = findViewById<TextView>(R.id.textCrearProyecto)
            windowTitle.text = "Editar proyecto"
            btnCrear.text = "Editar"

            val projectId = intent.getLongExtra("projectId", 0)
            val nombre = intent.getStringExtra("nombre") ?: ""
            val inicio = intent.getStringExtra("inicio") ?: ""
            val fin = intent.getStringExtra("fin") ?: ""
            val descripcion = intent.getStringExtra("descripcion") ?: ""
            val estado = intent.getStringExtra("estado") ?: ""

            findViewById<EditText>(R.id.nombreProyecto).setText(nombre)
            findViewById<EditText>(R.id.descripcionProyecto).setText(descripcion)
            findViewById<EditText>(R.id.inicioProyecto).setText(inicio)
            findViewById<EditText>(R.id.finProyecto).setText(fin)
            spinner1.setSelection(adapter1.getPosition(estado))
            btnCrear.setOnClickListener{
                performUpdateProject(projectId)
            }

        }
    }

    fun showDatePickerDialog(view: View) {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            // +1 porque enero es cero
            val selectedDate = day.toString() + "/" + (month+1) + "/" + year
            (view as EditText).setText(selectedDate)
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }
    private fun goToMenu(){
        val i = Intent(this, Menu::class.java)
        startActivity(i)
        finish()
    }

    private fun performCreateProject(){
        val preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
        var fechaInicioFinal = ""
        var fechaFinFinal = ""
        val urlBase = preferencesTest.getString("urlBase", "").toString()
        //Toast.makeText(applicationContext, "Debug server ${urlBase.toString()}", Toast.LENGTH_SHORT).show()
        val nombreProyecto = findViewById<EditText>(R.id.nombreProyecto).text.toString()
        if(nombreProyecto == ""){
            Toast.makeText(applicationContext, "El campo Nombre del proyecto es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }
        //Convierte la fecha a formato AÑO-MES-DIA 00:00:00
        val fechaInicio = findViewById<EditText>(R.id.inicioProyecto).text.toString().split("/")
        //Toast.makeText(applicationContext, "Debug fechaInicio ${fechaInicio.size}" , Toast.LENGTH_SHORT).show()
        if(fechaInicio.size < 3){
                //Toast.makeText(applicationContext, "DEBUG: FechaInicio empty", Toast.LENGTH_SHORT).show()
        }else{
            val diaInicioInt = fechaInicio[0].toInt()
            val mesInicioInt = fechaInicio[1].toInt()
            var diaInicio = "$diaInicioInt"
            var mesInicio = "$mesInicioInt"
            if(diaInicioInt < 10){
                diaInicio = "0$diaInicioInt"
            }
            if(mesInicioInt < 10){
                mesInicio = "0$mesInicioInt"
            }
            fechaInicioFinal = "${fechaInicio[2]}-$mesInicio-$diaInicio 00:00:00"
        }
        val fechaFin = findViewById<EditText>(R.id.finProyecto).text.toString().split("/")
        //Toast.makeText(applicationContext, "Debug fechaInicio ${fechaFin.size}" , Toast.LENGTH_SHORT).show()
        if(fechaFin.size < 3){
            //Toast.makeText(applicationContext, "DEBUG: FechaFin empty", Toast.LENGTH_SHORT).show()
        }else{
            val diaFinInt = fechaFin[0].toInt()
            val mesFinInt = fechaFin[1].toInt()
            var diaFin = "$diaFinInt"
            var mesFin = "$mesFinInt"
            if(diaFinInt < 10){
                diaFin = "0$diaFinInt"
            }
            if(mesFinInt < 10){
                mesFin = "0$mesFinInt"
            }
            fechaFinFinal = "${fechaFin[2]}-$mesFin-$diaFin 00:00:00"
        }
        //Log.e("DATE", "${fechaInicio[2]}-${fechaInicio[1]}-${fechaInicio[0]} 00:00:00")
        val createProjectRequest = ProjectRequest(
            nombre = nombreProyecto,
            inicio = fechaInicioFinal,
            fin = fechaFinFinal,
            descripcion = findViewById<EditText>(R.id.descripcionProyecto).text.toString(),
            estado = selectedState
        )

        val authHeader = "Bearer ${jwtHelper.getToken()}"

        ApiService.setBaseUrl(baseUrl().toString())
        val call = apiService?.createProject(authHeader, createProjectRequest)

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
    private fun performUpdateProject(projectId: Long){
        var fechaInicio: String = ""
        if(findViewById<EditText>(R.id.inicioProyecto).text.toString().isNotEmpty()){
            val ini = findViewById<EditText>(R.id.inicioProyecto).text.toString()
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = formatter.parse(ini)
            val calendar = Calendar.getInstance().apply { time = date }

            val diaInicioInt = calendar.get(Calendar.DAY_OF_MONTH)
            val mesInicioInt = calendar.get(Calendar.MONTH) + 1 // Sumar 1 porque en Calendar.MONTH enero es 0
            val añoInicio = calendar.get(Calendar.YEAR)

            val diaInicio = if (diaInicioInt < 10) "0$diaInicioInt" else "$diaInicioInt"
            val mesInicio = if (mesInicioInt < 10) "0$mesInicioInt" else "$mesInicioInt"

            fechaInicio = "$añoInicio-$mesInicio-$diaInicio 00:00:00"
        }

        var fechaFin: String = ""
        if(findViewById<EditText>(R.id.finProyecto).text.toString().isNotEmpty()){
            val fin = findViewById<EditText>(R.id.finProyecto).text.toString()
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = formatter.parse(fin)
            val calendar = Calendar.getInstance().apply { time = date }

            val diaFinInt = calendar.get(Calendar.DAY_OF_MONTH)
            val mesFinInt = calendar.get(Calendar.MONTH) + 1 // Sumar 1 porque en Calendar.MONTH enero es 0
            val añoFin = calendar.get(Calendar.YEAR)

            val diaFin = if (diaFinInt < 10) "0$diaFinInt" else "$diaFinInt"
            val mesFin = if (mesFinInt < 10) "0$mesFinInt" else "$mesFinInt"

            fechaFin = "$añoFin-$mesFin-$diaFin 00:00:00"
        }
        val updateProjectRequest = ProjectRequest(
            nombre = findViewById<EditText>(R.id.nombreProyecto).text.toString(),
            inicio = fechaInicio,
            fin = fechaFin,
            descripcion = findViewById<EditText>(R.id.descripcionProyecto).text.toString(),
            estado = selectedState
        )

        val authHeader = "Bearer ${jwtHelper.getToken()}"
        val call = apiService?.updateProject(projectId, authHeader, updateProjectRequest)

        if (call != null) {
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val registerResponse = response.body()
                    if(response.isSuccessful) {
                        if (registerResponse == null) {
                            Toast.makeText(
                                applicationContext,
                                "Se produjo un error en el servidor",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        Toast.makeText(applicationContext, "Proyecto actualizado correctamente", Toast.LENGTH_SHORT).show()
                        goToMenu()

                    } else {
                        // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                        Toast.makeText(applicationContext, "Error al actualizar el proyecto", Toast.LENGTH_SHORT).show()
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

