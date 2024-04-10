package com.gettasksdone.gettasksdone.ui.inBox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.databinding.FragmentInboxBinding
import com.gettasksdone.gettasksdone.model.Task

class InBoxFragment : Fragment() {

    private var _binding: FragmentInboxBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val jwtHelper = JwtHelper(context)
        val factory = InboxViewModelFactory(jwtHelper)
        val inboxViewModel = ViewModelProvider(this, factory).get(InboxViewModel::class.java)

        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textInBox

        // Observar la lista de tareas en el ViewModel y actualizar la interfaz de usuario
        inboxViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            // Actualiza la interfaz de usuario con las tareas obtenidas
            // Por ejemplo, puedes mostrar las tareas en un RecyclerView
            val descriptions = buildDescriptionsString(tasks)
            // Establecer la cadena en el TextView
            textView.text = descriptions
        }

        // Llamar al método para obtener las tareas
        inboxViewModel.getTasks()

        return root
    }

    private fun buildDescriptionsString(tasks: List<Task>): String {
        val descriptions = StringBuilder()
        for (task in tasks) {
            descriptions.append(task.descripcion)
            descriptions.append("\n") // Agregar un salto de línea después de cada descripción
        }
        return descriptions.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
