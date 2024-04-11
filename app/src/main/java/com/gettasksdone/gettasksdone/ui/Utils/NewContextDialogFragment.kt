package com.gettasksdone.gettasksdone.ui.Utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.gettasksdone.gettasksdone.R

class NewContextDialogFragment : DialogFragment() {
    interface NewContextDialogListener {
        fun onDialogPositiveClick(newContextName: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_new_context, null)

            builder.setView(dialogView)
                .setTitle("Nuevo Contexto")
                .setPositiveButton("Enviar") { _, _ ->
                    val editTextNewContext = dialogView.findViewById<EditText>(R.id.editTextNewContext)
                    val newContextName = editTextNewContext.text.toString()
                    (activity as NewContextDialogListener).onDialogPositiveClick(newContextName)
                }
                .setNegativeButton("Cancelar") { _, _ ->
                    dialog?.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}