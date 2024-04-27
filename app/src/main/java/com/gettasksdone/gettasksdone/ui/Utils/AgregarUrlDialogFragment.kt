package com.gettasksdone.gettasksdone.ui.Utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.gettasksdone.gettasksdone.R

class AgregarUrlDialogFragment: DialogFragment() {
    interface NewUrlDialogListener {
        fun onDialogPositiveClick(newUrl: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_new_url, null)

            builder.setView(dialogView)
                .setTitle("Agregar URL de servidor")
                .setPositiveButton("Enviar") { _, _ ->
                    val editTextNewUrl = dialogView.findViewById<EditText>(R.id.editTextNewUrl)
                    val newUrlName = editTextNewUrl.text.toString()
                    (activity as AgregarUrlDialogFragment.NewUrlDialogListener).onDialogPositiveClick(newUrlName)
                }
                .setNegativeButton("Cancelar") { _, _ ->
                    dialog?.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object{
        const val TAG = "AgregarUrlDialog"
    }
}