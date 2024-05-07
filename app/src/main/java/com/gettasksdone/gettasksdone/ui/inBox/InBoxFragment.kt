package com.gettasksdone.gettasksdone.ui.inBox

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.databinding.FragmentInboxBinding
import com.gettasksdone.gettasksdone.model.Task
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.gettasksdone.gettasksdone.io.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface TaskCompletionListener {
    fun onTaskCompleted()
}
class InBoxFragment : Fragment(), TaskCompletionListener {

    private var _binding: FragmentInboxBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Define inboxViewModel a nivel de clase
    private lateinit var inboxViewModel: InboxViewModel

    private val apiService: ApiService? by lazy {
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
        val viewModelScope = viewLifecycleOwner.lifecycleScope
        val factory = InboxViewModelFactory(jwtHelper, context, viewModelScope)
        inboxViewModel = ViewModelProvider(this, factory).get(InboxViewModel::class.java)

        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa el RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observa la lista de tareas en el ViewModel y actualiza la interfaz de usuario
        inboxViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            // Actualiza la interfaz de usuario con las tareas obtenidas
            // Configura el RecyclerView con un Adapter que muestra las tareas
            recyclerView.adapter =
                TaskAdapter(tasks, apiService, jwtHelper, this@InBoxFragment, this)
        }

        // Llama al método para obtener las tareas
        inboxViewModel.getTasks()

        // Añade el ItemTouchHelper al RecyclerView si está en modo online
        if(apiService != null){
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
                        val taskAdapter = recyclerView.adapter as TaskAdapter
                        val task = taskAdapter.tasks[position]

                        if(apiService != null){
                            AlertDialog.Builder(context).apply {
                                setTitle("Confirmación")
                                setMessage("¿Seguro que quieres eliminar la tarea '${task.titulo}'?")
                                setPositiveButton("Sí") { dialog, _ ->

                                    val authHeader = "Bearer ${jwtHelper.getToken()}"
                                    val call = apiService?.deleteTask(task.id, authHeader)

                                    if (call != null) {
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
                                                    Toast.makeText(context, "Tarea borrada correctamente", Toast.LENGTH_SHORT).show()
                                                    inboxViewModel.getTasks()
                                                } else {
                                                    // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                                                    Toast.makeText(context, "Error al borrar la tarea", Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                            override fun onFailure(call: Call<String>, t: Throwable) {
                                                Log.e("API_CALL", "Error en onFailure(): ${t.message}")

                                                Toast.makeText(context, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()

                                            }
                                        })
                                    }

                                    dialog.dismiss()

                                }
                                setNegativeButton("No") { dialog, _ ->
                                    // Si el usuario elige "No", simplemente vuelves a mostrar la tarea en la lista
                                    taskAdapter.notifyItemChanged(position)
                                    dialog.dismiss()
                                }
                            }.show()
                        }
                    }
                }

            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTaskCompleted() {
        // Llama a getTasks() para actualizar la lista de tareas
        inboxViewModel.getTasks()
    }
}


