package com.gettasksdone.gettasksdone.ui.contextos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.databinding.FragmentContextosBinding

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
        val factory = ContextosViewModelFactory(jwtHelper)
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
            recyclerView.adapter =
                ContextAdapter(contexts, this)
        }

        contextosViewModel.loadContexts()

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}