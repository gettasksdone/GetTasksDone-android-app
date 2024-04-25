package com.gettasksdone.gettasksdone.ui.inBox

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.databinding.FragmentInboxBinding
import com.gettasksdone.gettasksdone.model.Task
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import com.gettasksdone.gettasksdone.io.ApiService

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
        val factory = InboxViewModelFactory(jwtHelper)
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
                    val taskAdapter = recyclerView.adapter as TaskAdapter
                    val task = taskAdapter.tasks[position]

                    AlertDialog.Builder(context).apply {
                        setTitle("Confirmación")
                        setMessage("¿Seguro que quieres eliminar la tarea '${task.titulo}'?")
                        setPositiveButton("Sí") { dialog, _ ->
                            // Aquí llamas a la API para eliminar la tarea
                            // Recuerda manejar correctamente la respuesta de la API
                            // Si la tarea se elimina correctamente, puedes removerla de tu lista local y notificar al adaptador
                            //taskAdapter.tasks.removeAt(position)
                            //taskAdapter.notifyItemRemoved(position)
                            //dialog.dismiss()
                        }
                        setNegativeButton("No") { dialog, _ ->
                            // Si el usuario elige "No", simplemente vuelves a mostrar la tarea en la lista
                            taskAdapter.notifyItemChanged(position)
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
        inboxViewModel.getTasks()
    }
}


