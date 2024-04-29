package com.gettasksdone.gettasksdone.ui.contextos

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.AnadirTask
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.io.requests.TaskRequest
import com.gettasksdone.gettasksdone.model.Context
import com.gettasksdone.gettasksdone.model.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class ContextAdapter(
    var contexts: List<Context>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<ContextAdapter.ContextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContextViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_context_item, parent, false)
        return ContextViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContextViewHolder, position: Int) {
        val currentContext = contexts[position]
        holder.bind(currentContext)
    }

    override fun getItemCount() = contexts.size

    inner class ContextViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val nombreContextTextView: TextView = itemView.findViewById(R.id.textNombreContexto)
        private val context: android.content.Context? = itemView.context

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(context: Context) {
            nombreContextTextView.text = context.nombre
            // Aquí puedes agregar más lógica para personalizar la vista de cada contexto
        }

        override fun onClick(v: View?) {
            val context = contexts[adapterPosition]
            // Aquí puedes manejar el evento de clic en un contexto
        }
    }
}