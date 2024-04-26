import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.model.Project
import com.gettasksdone.gettasksdone.R
import android.content.Context
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.ui.inBox.TaskAdapter
import com.gettasksdone.gettasksdone.ui.inBox.TaskCompletionListener

class ProyectosAdapter(
    private val proyectos: List<Project>,
    private val fragment: Fragment,
    private val apiService: ApiService,
    private val jwtHelper: JwtHelper,
    private val context: Context,
    private val onTaskCompleted: TaskCompletionListener,

    ) : RecyclerView.Adapter<ProyectosAdapter.ProyectoViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(project: Project)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProyectoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_proyecto, parent, false)
        return ProyectoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProyectoViewHolder, position: Int) {
        val currentProject = proyectos[position]
        holder.bind(currentProject)
    }

    override fun getItemCount() = proyectos.size

    inner class ProyectoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val nombreProyectoTextView: TextView =
            itemView.findViewById(R.id.textNombreProyecto)
        private val numTareasTextView: TextView = itemView.findViewById(R.id.textNumeroTareas)

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(project: Project) {
            nombreProyectoTextView.text = project.nombre
            numTareasTextView.text = "Número de tareas: ${project.tareas.size}"
        }

        override fun onClick(v: View) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedProject = proyectos[position]
                // Obtener las tareas asociadas al proyecto
                val tasks = clickedProject.tareas

                // Configurar el RecyclerView con el adaptador de tareas
                val recyclerViewTasks = fragment.view?.findViewById<RecyclerView>(R.id.recyclerViewTasks)
                recyclerViewTasks?.layoutManager = LinearLayoutManager(context)
                recyclerViewTasks?.adapter = TaskAdapter(tasks, apiService, jwtHelper, fragment, onTaskCompleted)

                // Cambiar la visibilidad del RecyclerView de las tareas
                recyclerViewTasks?.visibility = View.VISIBLE

                // Cambiar la visibilidad del RecyclerView de los proyectos
                val recyclerViewProjects = fragment.view?.findViewById<RecyclerView>(R.id.recyclerViewProyectos)
                recyclerViewProjects?.visibility = View.GONE

                // Mostrar el botón de "Atrás"
                val buttonBack = fragment.view?.findViewById<Button>(R.id.buttonBack)
                buttonBack?.visibility = View.VISIBLE
            }
        }

        private fun showProjectDetailsDialog(context: Context, project: Project) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Detalles del proyecto")
            val detailsBuilder = StringBuilder()
            detailsBuilder.append("Nombre: ${project.nombre}\n")
            detailsBuilder.append("Inicio: ${project.inicio}\n")
            detailsBuilder.append("Fin: ${project.fin}\n")
            detailsBuilder.append("Descripción: ${project.descripcion}\n")
            detailsBuilder.append("Estado: ${project.estado}\n\n")
            detailsBuilder.append("Tareas:\n")
            for (task in project.tareas) {
                detailsBuilder.append("- ${task.titulo}\n")
            }
            builder.setMessage(detailsBuilder.toString())
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}
