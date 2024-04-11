package com.gettasksdone.gettasksdone.ui.inBox

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

    inner class TaskViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
        init {
            cardView.setOnClickListener {
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
        val cardView = CardView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            // Aquí puedes personalizar el aspecto de tu CardView
            setCardBackgroundColor(Color.LTGRAY)
            radius = 15f
            setContentPadding(25, 25, 25, 25)
            val linearLayout = LinearLayout(parent.context).apply {
                orientation = LinearLayout.HORIZONTAL
                addView(TextView(parent.context))
                addView(ImageButton(parent.context).apply {
                    setImageResource(R.drawable.ic_done) // Asegúrate de tener un recurso drawable llamado ic_done
                    setBackgroundColor(Color.TRANSPARENT)
                    setOnClickListener {
                        // Aquí puedes agregar la lógica para marcar la tarea como hecha
                    }
                })
            }
            addView(linearLayout)
        }
        return TaskViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val linearLayout = holder.cardView.getChildAt(0) as LinearLayout
        (linearLayout.getChildAt(0) as TextView).text = tasks[position].descripcion
    }

    override fun getItemCount() = tasks.size
}
