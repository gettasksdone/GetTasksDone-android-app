package com.gettasksdone.gettasksdone.ui.contextos


import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContextAdapter(
    var contexts: List<Context>,
    private val fragment: Fragment,
    private val jwtHelper: JwtHelper,
    private val onContextDeleted: (Long) -> Unit  // Añade esto
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

    inner class ContextViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val nombreContextTextView: TextView = itemView.findViewById(R.id.textNombreContexto)
        private val buttonEngranaje: ImageButton = itemView.findViewById(R.id.buttonEngranaje)
        private val context: android.content.Context? = itemView.context

        init {
            itemView.setOnClickListener(this)
            buttonEngranaje.setOnClickListener {
                // Aquí puedes manejar el evento de clic en el botón de engranaje
                val contextId = contexts[adapterPosition].id
                val contextName = nombreContextTextView.text.toString()
                (fragment as ContextosFragment).showEditDialog(contextId, contextName)
            }
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

    fun attachSwipeToDelete(recyclerView: RecyclerView) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // no soportamos mover elementos en este ejemplo
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Aquí puedes manejar el evento de deslizar para eliminar
                val contextId = contexts[viewHolder.adapterPosition].id
                onContextDeleted(contextId)  // Llama a la función pasada como parámetro cuando se confirme la eliminación
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}