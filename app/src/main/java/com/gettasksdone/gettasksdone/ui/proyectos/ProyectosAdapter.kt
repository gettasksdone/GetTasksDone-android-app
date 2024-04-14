import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gettasksdone.gettasksdone.model.Project
import com.gettasksdone.gettasksdone.R

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

        fun bind(project: Project) {
            nombreProyectoTextView.text = project.nombre
            numTareasTextView.text = "Número de tareas: ${project.tareas.size}"
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedProject = proyectos[position]
                //listener.onItemClick(clickedProject)
            }
        }
    }
}
