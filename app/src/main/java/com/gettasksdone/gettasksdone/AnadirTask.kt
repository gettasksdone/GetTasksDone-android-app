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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import kotlin.properties.Delegates

class AnadirTask : AppCompatActivity(), NewContextDialogFragment.NewContextDialogListener {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val jwtHelper: JwtHelper by lazy {
        JwtHelper(this)
    }
    private lateinit var et_fecha: EditText
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
        et_fecha = findViewById(R.id.et_fecha)
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
        btnCrear.setOnClickListener{
            performCreateTask()
        }
    }

    private fun goToMenu(){
        val i = Intent(this, Menu::class.java)
        startActivity(i)
        finish()
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
            prioridad = if (isStarred) 1 else 0,
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
                    goToMenu()

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

    override fun onDialogPositiveClick(newContextName: String) {
        // Aquí puedes hacer lo que quieras con el nuevo nombre del contexto
        // Por ejemplo, puedes agregarlo a la lista de contextos y actualizar el Spinner
        // En este ejemplo, simplemente lo mostraremos en un Toast
        //Toast.makeText(applicationContext, "Nuevo contexto: $newContextName", Toast.LENGTH_SHORT).show()

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
            et_fecha.setText(selectedDate)
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
