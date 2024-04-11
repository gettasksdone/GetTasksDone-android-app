package com.gettasksdone.gettasksdone.ui.inBox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.databinding.FragmentInboxBinding
import com.gettasksdone.gettasksdone.model.Task

class InBoxFragment : Fragment() {

    private var _binding: FragmentInboxBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Define inboxViewModel a nivel de clase
    private lateinit var inboxViewModel: InboxViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val jwtHelper = JwtHelper(context)
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
            recyclerView.adapter = TaskAdapter(tasks, this)
        }

        // Llama al método para obtener las tareas
        inboxViewModel.getTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class TaskAdapter(private val tasks: List<Task>, private val fragment: Fragment) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView) {
        init {
            textView.setOnClickListener {
                val task = tasks[adapterPosition]

                // Encuentra el RecyclerView, el TextView y el botón en la actividad
                val recyclerView = fragment.requireActivity().findViewById<RecyclerView>(R.id.recyclerView)
                val taskTextView = fragment.requireActivity().findViewById<TextView>(R.id.taskTextView)
                val backButton = fragment.requireActivity().findViewById<Button>(R.id.backButton)

                // Oculta el RecyclerView
                recyclerView.visibility = View.GONE

                // Muestra el TextView y establece su texto a la descripción de la tarea
                taskTextView.text = task.descripcion
                taskTextView.visibility = View.VISIBLE

                // Muestra el botón
                backButton.visibility = View.VISIBLE

                // Establece un OnClickListener en el botón para ocultar el TextView y mostrar el RecyclerView
                backButton.setOnClickListener {
                    recyclerView.visibility = View.VISIBLE
                    taskTextView.visibility = View.GONE
                    backButton.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val textView = TextView(parent.context)
        return TaskViewHolder(textView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.textView.text = tasks[position].descripcion
    }

    override fun getItemCount() = tasks.size
}
