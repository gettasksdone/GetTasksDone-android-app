package com.gettasksdone.gettasksdone.ui.proyectos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.databinding.FragmentProyectosBinding

class ProyectosFragment : Fragment() {

private var _binding: FragmentProyectosBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val proyectosViewModel =
            ViewModelProvider(this).get(ProyectosViewModel::class.java)

    _binding = FragmentProyectosBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textProyectos
    proyectosViewModel.text.observe(viewLifecycleOwner) {
    textView.text = it
    }

    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}