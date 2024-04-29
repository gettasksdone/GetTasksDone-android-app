package com.gettasksdone.gettasksdone.ui.contextos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.databinding.FragmentContextosBinding

class ContextosFragment : Fragment() {

private var _binding: FragmentContextosBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contextosViewModel =
            ViewModelProvider(this).get(ContextosViewModel::class.java)

        _binding = FragmentContextosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Llama a loadContexts para cargar los contextos desde la API
        contextosViewModel.loadContexts()

        // Observa la lista de contextos y actualiza la interfaz de usuario cuando cambie
        contextosViewModel.contextList.observe(viewLifecycleOwner) { contextList ->
            // Aquí puedes actualizar tu interfaz de usuario con la lista de contextos
            // ...
        }

        return root
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}