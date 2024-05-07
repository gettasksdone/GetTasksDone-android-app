package com.gettasksdone.gettasksdone.ui.agendado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.databinding.FragmentAgendadoBinding
import com.gettasksdone.gettasksdone.databinding.FragmentInboxBinding
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.ui.esperando.EsperandoViewModel
import com.gettasksdone.gettasksdone.ui.inBox.InboxViewModelFactory
import com.gettasksdone.gettasksdone.ui.inBox.TaskAdapter
import com.gettasksdone.gettasksdone.ui.inBox.TaskCompletionListener

class AgendadoFragment : Fragment(), TaskCompletionListener {

  private var _binding: FragmentInboxBinding? = null

  private val binding get() = _binding!!

  private lateinit var agendadoViewModel: AgendadoViewModel

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
    agendadoViewModel = ViewModelProvider(this, factory).get(AgendadoViewModel::class.java)

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
    agendadoViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
      // Actualiza la interfaz de usuario con las tareas obtenidas
      // Configura el RecyclerView con un Adapter que muestra las tareas
      recyclerView.adapter = TaskAdapter(tasks, apiService, jwtHelper, this@AgendadoFragment, this)
    }

    // Llama al método para obtener las tareas
    agendadoViewModel.getTasks()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onTaskCompleted() {
    // Llama a getTasks() para actualizar la lista de tareas
    agendadoViewModel.getTasks()
  }

}