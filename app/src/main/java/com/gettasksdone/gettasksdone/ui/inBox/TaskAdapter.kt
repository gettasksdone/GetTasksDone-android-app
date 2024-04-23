package com.gettasksdone.gettasksdone.ui.inBox

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.gettasksdone.gettasksdone.model.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class TaskAdapter(
    private val tasks: List<Task>,
    private val apiService: ApiService,
    private val jwtHelper: JwtHelper,
    private val fragment: Fragment,
    private val taskCompletionListener: TaskCompletionListener
) :
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
        private val iconoImageView: ImageView = itemView.findViewById(R.id.icono)

        init {
            itemView.setOnClickListener(this)
            iconoImageView.setOnClickListener { onUpdateIconClicked(bindingAdapterPosition) }
            itemView.setOnClickListener {
                editTask(bindingAdapterPosition)
            }
        }

        fun bind(task: Task) {
            val capitalizedTaskTitle = task.titulo.split(" ").joinToString(" ") { it.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            } }
            nombreTaskTextView.text = task.titulo

            // Asigna el emoticono correcto en función de la prioridad
            val emoticonoImageView: ImageView = itemView.findViewById(R.id.emoticono)
            emoticonoImageView.setImageResource(when (task.estado) {
                "empezar" -> R.drawable.ic_menu_slideshow // Nombre del recurso de imagen para el emoticono de "empezar"
                "esperando" -> R.drawable.mug_hot_solid // Nombre del recurso de imagen para el emoticono de "esperando"
                "algun dia" -> R.drawable.mountain_sun_solid // Nombre del recurso de imagen para el emoticono de "algun dia"
                else -> R.drawable.ic_menu_gallery // Nombre del recurso de imagen para un valor por defecto
            })

            // Cambia el color de fondo de la CardView en función de la prioridad
            val cardView = itemView as CardView
            when (task.prioridad) {
                0 -> {
                    val gradientDrawable = GradientDrawable(
                        GradientDrawable.Orientation.RIGHT_LEFT,
                        intArrayOf(Color.parseColor("#7f8c8d"), Color.parseColor("#95a5a6")) // Verde semitransparente
                    )
                    gradientDrawable.cornerRadius = 16.dpToPx(context)
                    cardView.background = gradientDrawable
                }
                1 -> {
                    val gradientDrawable = GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT,
                        intArrayOf(Color.parseColor("#95a5a6"), Color.parseColor("#7f8c8d")) // Rojo semitransparente
                    )
                    gradientDrawable.cornerRadius = 16.dpToPx(context)
                    cardView.background = gradientDrawable
                }
            }
        }

        override fun onClick(v: View?) {
            val task = tasks[adapterPosition]
        }
        fun Int.dpToPx(context: Context): Float {
            return this * context.resources.displayMetrics.density
        }

        private fun onUpdateIconClicked(position: Int) {
            val task = tasks[position]
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Confirmación")
            alertDialogBuilder.setMessage("Seguro que quieres marcar la tarea '${task.titulo}' como completada?")
            alertDialogBuilder.setPositiveButton("Aceptar") { dialog, _ ->

                val createTaskRequest = TaskRequest(
                    titulo = task.titulo,
                    descripcion = task.descripcion,
                    estado = "completado",
                    prioridad = task.prioridad,
                    contexto = task.contexto,
                    vencimiento = task.vencimiento,
                )

                val authHeader = "Bearer ${jwtHelper.getToken()}"
                val call = apiService.updateTask(task.id, authHeader, createTaskRequest)

                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val registerResponse = response.body()
                        if (response.isSuccessful) {
                            if (registerResponse == null) {
                                Toast.makeText(
                                    context,
                                    "Se produjo un error en el servidor (onResponse)",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return
                            }
                            Toast.makeText(context, "Tarea completada", Toast.LENGTH_SHORT).show()
                            //goToMenu()
                            taskCompletionListener.onTaskCompleted()

                        } else {
                            // Añade aquí el manejo del caso en el que la respuesta HTTP no es exitosa
                            Toast.makeText(
                                context,
                                "Error al actualizar la tarea",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("API_CALL", "Error en onFailure(): ${t.message}")

                        Toast.makeText(
                            context,
                            "Se produjo un error en el servidor",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                })

                notifyItemChanged(position)

                //Toast.makeText(context, "Tarea completada", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        fun editTask(position: Int) {
            val task = tasks[position]
            val intent = Intent(context, AnadirTask::class.java)
            intent.putExtra("editMode", true)
            intent.putExtra("taskId", task.id)
            intent.putExtra("taskTitle", task.titulo)
            intent.putExtra("taskDescription", task.descripcion)
            intent.putExtra("taskDueDate", task.vencimiento)
            intent.putExtra("taskContextId", task.contexto.id)
            intent.putExtra("taskState", task.estado)
            // Agrega más extras según sea necesario
            context.startActivity(intent)
        }

    }


}