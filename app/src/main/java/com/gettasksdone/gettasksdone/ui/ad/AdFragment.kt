package com.gettasksdone.gettasksdone.ui.ad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gettasksdone.gettasksdone.databinding.FragmentAdBinding

class AdFragment : Fragment() {

private var _binding: FragmentAdBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val adViewModel =
            ViewModelProvider(this).get(AdViewModel::class.java)

    _binding = FragmentAdBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textAd
    adViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}