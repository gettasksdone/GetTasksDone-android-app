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

class ProyectosAdapter(
    private val proyectos: List<Project>,
    private val fragment: Fragment
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

    inner class ProyectoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val nombreProyectoTextView: TextView = itemView.findViewById(R.id.textNombreProyecto)
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
                showProjectDetailsDialog(v.context, clickedProject)
            }
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
            detailsBuilder.append("- ${task.descripcion}\n")
        }
        builder.setMessage(detailsBuilder.toString())
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
