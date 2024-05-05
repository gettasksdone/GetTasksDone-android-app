package com.gettasksdone.gettasksdone.ui.contextos

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.databinding.FragmentContextosBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContextosFragment : Fragment() {

    private var _binding: FragmentContextosBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var jwtHelper: JwtHelper
    private lateinit var contextosViewModel: ContextosViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val context = requireContext()
        jwtHelper = JwtHelper(context)
        val viewModelScope = viewLifecycleOwner.lifecycleScope
        val factory = ContextosViewModelFactory(jwtHelper, context, viewModelScope)
        contextosViewModel = ViewModelProvider(this, factory).get(ContextosViewModel::class.java)

        _binding = FragmentContextosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        contextosViewModel.contextList.observe(viewLifecycleOwner) {contexts ->
            recyclerView.adapter = ContextAdapter(contexts, this, jwtHelper) { contextId ->
                showDeleteConfirmationDialog(contextId)
            }
            (recyclerView.adapter as ContextAdapter).attachSwipeToDelete(recyclerView)
        }

        contextosViewModel.loadContexts()

    }
    fun showEditDialog(contextId: Long, contextName: String) {
        val editText = EditText(requireContext())
        editText.setText(contextName)

        AlertDialog.Builder(requireContext())
            .setTitle("Editar nombre del contexto")
            .setView(editText)
            .setPositiveButton("Guardar") { dialog, _ ->
                // Si el usuario confirma la edición, llama a la función para actualizar el nombre del contexto
                val newName = editText.text.toString()
                contextosViewModel.updateContextName(contextId, newName)
                dialog.dismiss()
            }
            .setNegativeButton("Atrás") { dialog, _ ->
                // Si el usuario cancela la edición, simplemente cierra el diálogo
                dialog.cancel()
            }
            .show()
    }
    private fun showDeleteConfirmationDialog(contextId: Long) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar este contexto? Esto borrará todas las tareas y proyectos asociados.")
            .setPositiveButton("Sí") { dialog, _ ->
                // Si el usuario confirma la eliminación, llama a la función para eliminar el contexto
                contextosViewModel.deleteContext(contextId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                // Si el usuario cancela la eliminación, simplemente cierra el diálogo y recarga los contextos
                dialog.cancel()
                contextosViewModel.loadContexts()
            }
            .show()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}