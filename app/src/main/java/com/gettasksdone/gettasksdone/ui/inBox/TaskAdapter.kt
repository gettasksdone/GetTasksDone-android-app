package com.gettasksdone.gettasksdone.ui.inBox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.model.Task

class TaskAdapter(
    private val tasks: List<Task>,
    private val fragment: Fragment) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_list, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]
        holder.bind(currentTask)
    }

    override fun getItemCount() = tasks.size

    inner class TaskViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val nombreTaskTextView: TextView = itemView.findViewById(R.id.textNombreTarea)
        private val context: Context = itemView.context

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            nombreTaskTextView.text = task.descripcion
        }

        override fun onClick(v: View?) {
            val task = tasks[adapterPosition]
            val taskDetails = buildTaskDetails(task)
            Toast.makeText(context, taskDetails, Toast.LENGTH_LONG).show()
        }
    }
}

private fun buildTaskDetails(task: Task): String {
    val builder = StringBuilder()
    builder.append("ID: ${task.id}\n")
    builder.append("Descripción: ${task.descripcion}\n")
    builder.append("Estado: ${task.estado}\n")
    builder.append("Prioridad: ${task.prioridad}\n")
    // Agrega más detalles según sea necesario

    return builder.toString()
}