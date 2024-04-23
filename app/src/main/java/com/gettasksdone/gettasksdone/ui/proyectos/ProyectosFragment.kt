// ProyectosFragment.kt
package com.gettasksdone.gettasksdone.ui.proyectos

import ProyectosAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.databinding.FragmentProyectosBinding
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.ui.inBox.TaskCompletionListener

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
