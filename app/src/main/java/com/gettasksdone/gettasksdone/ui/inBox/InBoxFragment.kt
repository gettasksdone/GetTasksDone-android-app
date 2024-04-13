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


