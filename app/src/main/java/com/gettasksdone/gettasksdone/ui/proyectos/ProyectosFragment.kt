// ProyectosFragment.kt
package com.gettasksdone.gettasksdone.ui.proyectos

import ProyectosAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.databinding.FragmentProyectosBinding
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.ui.inBox.TaskAdapter
import com.gettasksdone.gettasksdone.ui.inBox.TaskCompletionListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProyectosFragment : Fragment(), TaskCompletionListener {

    private var _binding: FragmentProyectosBinding? = null
    private val binding get() = _binding!!

    private lateinit var proyectosViewModel: ProyectosViewModel
    private lateinit var proyectosAdapter: ProyectosAdapter

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private lateinit var jwtHelper: JwtHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val context = requireContext()
        jwtHelper = JwtHelper(context)
        val factory = ProjectViewModelFactory(jwtHelper)

        // Inicializar ViewModel
        proyectosViewModel = ViewModelProvider(this, factory).get(ProyectosViewModel::class.java)

        _binding = FragmentProyectosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewProyectos)
        recyclerView.layoutManager = LinearLayoutManager(context)

        proyectosViewModel.projects.observe(viewLifecycleOwner) { proyectos ->
            recyclerView.adapter = ProyectosAdapter(proyectos, this, apiService, jwtHelper, requireContext(), this)
        }

        proyectosViewModel.getProjects()

        val buttonBack = view.findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            // Cambiar la visibilidad del RecyclerView de las tareas
            val recyclerViewTasks = view.findViewById<RecyclerView>(R.id.recyclerViewTasks)
            recyclerViewTasks.visibility = View.GONE

            // Cambiar la visibilidad del RecyclerView de los proyectos
            val recyclerViewProjects = view.findViewById<RecyclerView>(R.id.recyclerViewProyectos)
            recyclerViewProjects.visibility = View.VISIBLE

            // Ocultar el botón de "Atrás"
            buttonBack.visibility = View.GONE

            // Aquí puedes agregar más lógica si es necesario
            // Por ejemplo, podrías querer actualizar la lista de proyectos
            proyectosViewModel.getProjects()
        }
        // Añade el ItemTouchHelper al RecyclerView
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val proyectosAdapter = recyclerView.adapter as ProyectosAdapter
                    val proyecto = proyectosAdapter.proyectos[position]

                    AlertDialog.Builder(context).apply {
                        setTitle("Confirmación")
                        setMessage("¿Seguro que quieres eliminar el proyecto?${proyecto.id}'?")
                        setPositiveButton("Sí") { dialog, _ ->

                            val authHeader = "Bearer ${jwtHelper.getToken()}"
                            val call = apiService.deleteProject(proyecto.id, authHeader)
                            call.enqueue(object : Callback<String> {
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    val registerResponse = response.body()
                                    if(response.isSuccessful) {
                                        if (registerResponse == null) {
                                            Toast.makeText(
                                                context,
                                                "Se produjo un error en el servidor",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return
                                        }
                                        Toast.makeText(context, "Proyecto borrado correctamente", Toast.LENGTH_SHORT).show()
                                        proyectosViewModel.getProjects()
                                    } else {
                                        // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                                        Toast.makeText(context, "Error al borrar el proyecto", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.e("API_CALL", "Error en onFailure(): ${t.message}")

                                    Toast.makeText(context, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()

                                }
                            })

                            dialog.dismiss()

                        }
                        setNegativeButton("No") { dialog, _ ->
                            // Si el usuario elige "No", simplemente vuelves a mostrar la tarea en la lista
                            proyectosAdapter.notifyItemChanged(position)
                            dialog.dismiss()
                        }
                    }.show()
                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTaskCompleted() {
        // Llama a getTasks() para actualizar la lista de tareas
        //esperandoViewModel.getTasks()
    }
}
