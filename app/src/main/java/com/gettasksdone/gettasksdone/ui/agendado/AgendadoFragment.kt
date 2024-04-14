package com.gettasksdone.gettasksdone.ui.agendado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.databinding.FragmentAgendadoBinding

class AgendadoFragment : Fragment() {

private var _binding: FragmentAgendadoBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val agendadoViewModel =
            ViewModelProvider(this).get(AgendadoViewModel::class.java)

    _binding = FragmentAgendadoBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textAgendado
    agendadoViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}