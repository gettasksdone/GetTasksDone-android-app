package com.gettasksdone.gettasksdone

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.io.requests.TaskRequest
import com.gettasksdone.gettasksdone.model.Context
import com.gettasksdone.gettasksdone.model.Project
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class AnadirTask : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val jwtHelper: JwtHelper by lazy {
        JwtHelper(this)
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinner1 = findViewById(R.id.contexto)
        adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter1
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "Otro...") {
                    val editText: EditText = findViewById(R.id.contextoOtro)
                    editText.visibility = View.VISIBLE
                } else {
                    val editText: EditText = findViewById(R.id.contextoOtro)
                    editText.visibility = View.GONE
                    if (position == contextList.size) {
                        openNewContextActivity()
                    } else {
                        selectedContext = contextList[position].id
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar el caso en que no se seleccione nada
            }
        }
        // Asumiendo que esta es tu lista de contextos
        val contextos = listOf("Contexto 1", "Contexto 2", "Contexto 3", "Otro...")

        // Creas un ArrayAdapter usando la lista de contextos
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, contextos)

        // Especificas el layout a usar cuando la lista de opciones aparece
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Aplicas el adapter a tu Spinner
        spinner1.adapter = adapter

       // loadContexts()

        val estados = mutableListOf("empezar", "Otro...")
        spinner2 = findViewById(R.id.estado)
        adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "Otro...") {
                    val editText: EditText = findViewById(R.id.estadoOtro)
                    editText.visibility = View.VISIBLE
                } else {
                    val editText: EditText = findViewById(R.id.estadoOtro)
                    editText.visibility = View.GONE
                    selectedState = selectedItem
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar el caso en que no se seleccione nada
            }
        }

        spinner3 = findViewById(R.id.proyecto)
        adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner3.adapter = adapter3
        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "Otro...") {
                    val editText: EditText = findViewById(R.id.proyectoOtro)
                    editText.visibility = View.VISIBLE
                } else {
                    val editText: EditText = findViewById(R.id.proyectoOtro)
                    editText.visibility = View.GONE
                    selectedProject = projectList[position].id
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar el caso en que no se seleccione nada
            }
        }

        loadProjects()

        val btnCrear = findViewById<Button>(R.id.crearTarea)
        btnCrear.setOnClickListener{
            performCreateTask()
        }
    }

    private fun performCreateTask(){

        val etTitulo = findViewById<EditText>(R.id.descripcion).text.toString()

        val contexto = Context(
                id = selectedContext,
                nombre = ""
        )

        val createTaskRequest = TaskRequest(
            descripcion = etTitulo,
            estado = selectedState,
            prioridad = 0,
            contexto = contexto,
            vencimiento = null,
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
                    Toast.makeText(applicationContext, "Nota creada correctamente", Toast.LENGTH_SHORT).show()

                } else {
                    // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                    Toast.makeText(applicationContext, "Error al crear la nota", Toast.LENGTH_SHORT).show()
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

    fun onStarClick(view: View) {
        // Descomentar cuando este hecho lo del booleano
        // val starButton = view as ImageButton
        // val isStarred = // obtén el valor booleano de alguna parte
        // val color = if (isStarred) Color.YELLOW else Color.GRAY
        // starButton.setColorFilter(color)
    }

    private fun openNewContextActivity() {
        //la idea es abrir una nueva pantalla o algo donde se pueda crear el contexto...o no se como se haria
        Toast.makeText(applicationContext, "CREAR NUEVO CONTEXTO TO BE DONE!", Toast.LENGTH_SHORT).show()

    }
}
