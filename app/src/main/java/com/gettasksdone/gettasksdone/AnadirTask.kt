package com.gettasksdone.gettasksdone

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.io.requests.ContextRequest
import com.gettasksdone.gettasksdone.io.requests.TaskRequest
import com.gettasksdone.gettasksdone.model.Context
import com.gettasksdone.gettasksdone.model.Project
import com.gettasksdone.gettasksdone.ui.Utils.NewContextDialogFragment
import com.gettasksdone.gettasksdone.util.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.properties.Delegates

class AnadirTask : AppCompatActivity(), NewContextDialogFragment.NewContextDialogListener {

    private val apiService: ApiService by lazy {
        val url = baseUrl()
        ApiService.setBaseUrl(url.toString())
        ApiService.create()
    }

    private fun baseUrl(): String? {
        val  preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
        val urlBase = preferencesTest.getString("urlBase", "")
        Toast.makeText(applicationContext, "Debug server $urlBase", Toast.LENGTH_SHORT).show()


        return urlBase

    }

    private val jwtHelper: JwtHelper by lazy {
        JwtHelper(this)
    }
    private lateinit var etFecha: EditText
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner
    private lateinit var adapter1: ArrayAdapter<String>
    private lateinit var adapter2: ArrayAdapter<String>
    private lateinit var adapter3: ArrayAdapter<String>
    private var selectedContext by Delegates.notNull<Long>()
    private lateinit var selectedState: String
    private var selectedProject by Delegates.notNull<Long>()
    private var contextList: List<Context> = listOf()
    private var projectList: List<Project> = listOf()
    private var isStarred = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etFecha = findViewById(R.id.et_fecha)
        spinner1 = findViewById(R.id.contexto)
        adapter1 = ArrayAdapter(this, R.layout.spinner_list, mutableListOf())
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter1
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                if (selectedItem == "Crear nuevo contexto") {
                    openNewContextActivity()
                } else {
                    selectedContext = contextList[position].id
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar el caso en que no se seleccione nada
            }
        }

        loadContexts()



        val estados = mutableListOf("empezar", "esperando", "algún día")
        spinner2 = findViewById(R.id.estado)
        adapter2 = ArrayAdapter(this, R.layout.spinner_list, estados)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                selectedState = selectedItem
                //no se pueden crear estados nuevos
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar el caso en que no se seleccione nada
            }
        }

        spinner3 = findViewById(R.id.proyecto)
        adapter3 = ArrayAdapter(this, R.layout.spinner_list, mutableListOf())
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner3.adapter = adapter3
        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedProject = projectList[position].id
                //vamos a simplificar y que de momento no se puedan crear nuevos proyectos desde aqui...
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar el caso en que no se seleccione nada
            }
        }

        loadProjects()

        val starButton = findViewById<ImageButton>(R.id.starButton)
        starButton.setOnClickListener {
            val newColor = if (isStarred) Color.YELLOW else Color.GRAY
            starButton.backgroundTintList = ColorStateList.valueOf(newColor)
            isStarred = !isStarred
        }

        val btnCrear = findViewById<Button>(R.id.crearTarea)


        if (intent.getBooleanExtra("editMode", false)) {
            val windowTitle = findViewById<TextView>(R.id.textCrearTarea)
            windowTitle.text = "Editar tarea"
            btnCrear.text = "Editar"

            // Modo de edición: recuperar valores de la tarea seleccionada
            val taskId = intent.getLongExtra("taskId", 0)
            val taskTitle = intent.getStringExtra("taskTitle") ?: ""
            val taskDescription = intent.getStringExtra("taskDescription") ?: ""
            val taskDueDate = intent.getStringExtra("taskDueDate") ?: ""
            val taskContextId = intent.getLongExtra("taskContextId", 0)
            val taskState = intent.getStringExtra("taskState") ?: ""

            // Rellenar los campos del formulario con los valores de la tarea seleccionada
            findViewById<EditText>(R.id.titulo).setText(taskTitle)
            findViewById<EditText>(R.id.etDescripcion).setText(taskDescription)
            findViewById<EditText>(R.id.et_fecha).setText(taskDueDate)
            // Configurar los valores seleccionados en los Spinners
            // (puedes necesitar cargar las listas de contextos y proyectos nuevamente para tener la selección correcta)
            spinner1.setSelection(adapter1.getPosition(taskContextId.toString()))
            spinner2.setSelection(adapter2.getPosition(taskState))
            btnCrear.setOnClickListener{
                performUpdateTask(taskId)
            }
        }else{
            btnCrear.setOnClickListener{
                performCreateTask()
            }
        }
    }

    private fun goToMenu(){
        val i = Intent(this, Menu::class.java)
        startActivity(i)
        finish()
    }
    private fun performCreateTask(){

        val  preferencesTest = PreferenceHelper.defaultPrefs(applicationContext)
        val urlBase = preferencesTest.getString("urlBase", "")
        Toast.makeText(applicationContext, "Debug server $urlBase", Toast.LENGTH_SHORT).show()

        val etTitulo = findViewById<EditText>(R.id.titulo).text.toString()

        if(etTitulo == ""){

            Toast.makeText(applicationContext, "El campo Titulo de la tarea es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }

        val etDescripcion = findViewById<EditText>(R.id.etDescripcion).text.toString()
        var vencimiento: String? = null
        if(findViewById<EditText>(R.id.et_fecha).text.toString().isNotEmpty()){
            val fechaVencimiento = findViewById<EditText>(R.id.et_fecha).text.toString().split("/")
            val diaVencimientoInt = fechaVencimiento[0].toInt()
            val mesVencimientoInt = fechaVencimiento[1].toInt()
            var diaVencimiento = "$diaVencimientoInt"
            var mesVencimiento = "$mesVencimientoInt"
            if(diaVencimientoInt < 10){
                diaVencimiento = "0$diaVencimientoInt"
            }
            if(mesVencimientoInt < 10){
                mesVencimiento = "0$mesVencimientoInt"
            }
            vencimiento = "${fechaVencimiento[2]}-$mesVencimiento-$diaVencimiento 00:00:00"
        }
        val contexto = Context(
                id = selectedContext,
                nombre = ""
        )

        val createTaskRequest = TaskRequest(
            titulo = etTitulo,
            descripcion = etDescripcion,
            estado = selectedState,
            prioridad = if (isStarred) 1 else 0,
            contexto = contexto,
            vencimiento = vencimiento,
        )

        val authHeader = "Bearer ${jwtHelper.getToken()}"
        val call = apiService.createTask(authHeader, selectedProject, createTaskRequest)

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
                    Toast.makeText(applicationContext, "Tarea creada correctamente", Toast.LENGTH_SHORT).show()
                    goToMenu()

                } else {
                    // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                    Toast.makeText(applicationContext, "Error al crear la tarea", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API_CALL", "Error en onFailure(): ${t.message}")

                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun performUpdateTask(taskId: Long){

        val etTitulo = findViewById<EditText>(R.id.titulo).text.toString()
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion).text.toString()
        var vencimiento: String? = null
        if(findViewById<EditText>(R.id.et_fecha).text.toString().isNotEmpty()){
            val fechaVencimiento = findViewById<EditText>(R.id.et_fecha).text.toString()
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = formatter.parse(fechaVencimiento)
            val calendar = Calendar.getInstance().apply {
                if (date != null) {
                    time = date
                }
            }

            val diaVencimientoInt = calendar.get(Calendar.DAY_OF_MONTH)
            val mesVencimientoInt = calendar.get(Calendar.MONTH) + 1 // Sumar 1 porque en Calendar.MONTH enero es 0
            val anioVencimiento = calendar.get(Calendar.YEAR)

            val diaVencimiento = if (diaVencimientoInt < 10) "0$diaVencimientoInt" else "$diaVencimientoInt"
            val mesVencimiento = if (mesVencimientoInt < 10) "0$mesVencimientoInt" else "$mesVencimientoInt"

            vencimiento = "$anioVencimiento-$mesVencimiento-$diaVencimiento 00:00:00"
        }

        val contexto = Context(
            id = selectedContext,
            nombre = ""
        )

        val updateTaskRequest = TaskRequest(
            titulo = etTitulo,
            descripcion = etDescripcion,
            estado = selectedState,
            prioridad = if (isStarred) 1 else 0,
            contexto = contexto,
            vencimiento = vencimiento,
        )

        val authHeader = "Bearer ${jwtHelper.getToken()}"
        val call = apiService.updateTask(taskId, authHeader, updateTaskRequest)

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
                    Toast.makeText(applicationContext, "Tarea actualizada correctamente", Toast.LENGTH_SHORT).show()
                    goToMenu()

                } else {
                    // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                    Toast.makeText(applicationContext, "Error al actualizar la tarea", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API_CALL", "Error en onFailure(): ${t.message}")

                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun loadContexts() {
        val authHeader = "Bearer ${jwtHelper.getToken()}"
        val call = apiService.getContexts(authHeader)

        call.enqueue(object : Callback<List<Context>> {
            override fun onResponse(call: Call<List<Context>>, response: Response<List<Context>>) {
                if (response.isSuccessful) {
                    val contextResponse = response.body()
                    if (contextResponse != null) {
                        contextList = contextResponse
                        val contextNames = contextResponse.map { it.nombre }.toMutableList()
                        contextNames.add("Crear nuevo contexto")
                        adapter1.clear()
                        adapter1.addAll(contextNames)
                    } else {
                        Toast.makeText(applicationContext, "La respuesta de la API está vacía", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Error al obtener los contextos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Context>>, t: Throwable) {
                Log.e("API_CALL", "Error en onFailure(): ${t.message}")
                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadProjects() {
        val authHeader = "Bearer ${jwtHelper.getToken()}"
        val call = apiService.getProjects(authHeader)

        call.enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                if (response.isSuccessful) {
                    val projectResponse = response.body()
                    if (projectResponse != null) {
                        projectList = projectResponse
                        val projectNames = projectResponse.map { it.nombre }
                        adapter3.clear()
                        adapter3.addAll(projectNames)
                    } else {
                        Toast.makeText(applicationContext, "La respuesta de la API está vacía", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Error al obtener los proyectos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                Log.e("API_CALL", "Error en onFailure(): ${t.message}")
                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDialogPositiveClick(newContextName: String) {
        // Aquí puedes hacer lo que quieras con el nuevo nombre del contexto
        // Por ejemplo, puedes agregarlo a la lista de contextos y actualizar el Spinner
        // En este ejemplo, simplemente lo mostraremos en un Toast
        //Toast.makeText(applicationContext, "Nuevo contexto: $newContextName", Toast.LENGTH_SHORT).show()
        if(newContextName == ""){
            Toast.makeText(applicationContext, "El contexto debe tener un nombre", Toast.LENGTH_SHORT).show()
            openNewContextActivity()
            return
        }
        val createContextoRequest = ContextRequest(
            nombre = newContextName
        )

        val authHeader = "Bearer ${jwtHelper.getToken()}"
        val call = apiService.createContext(authHeader, createContextoRequest)
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
                    Toast.makeText(applicationContext, "Contexto creado correctamente", Toast.LENGTH_SHORT).show()
                    loadContexts()

                } else {
                    // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                    Toast.makeText(applicationContext, "Error al crear el contexto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API_CALL", "Error en onFailure(): ${t.message}")

                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()

            }
        })

    }

    private fun openNewContextDialog() {
        val dialog = NewContextDialogFragment()
        dialog.show(supportFragmentManager, "NewContextDialogFragment")
    }

    private fun openNewContextActivity() {
        openNewContextDialog()
    }
    fun showDatePickerDialog(view: View) {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            // +1 porque enero es cero
            val selectedDate = day.toString() + "/" + (month+1) + "/" + year
            etFecha.setText(selectedDate)
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }
}

class DatePickerFragment : DialogFragment() {

    private var listener: DatePickerDialog.OnDateSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(), listener, year, month, day)
    }

    companion object {
        fun newInstance(listener: DatePickerDialog.OnDateSetListener): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.listener = listener
            return fragment
        }
    }
}
