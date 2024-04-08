package com.gettasksdone.gettasksdone

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gettasksdone.gettasksdone.R

class AnadirTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_anadir_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Spinner
        val contextos = mutableListOf("contexto", "Contexto 1", "Contexto 2", "añadir nuevo contexto") //contextos de ejemplo luego seran los de la API
        val spinner: Spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, contextos)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val seleccionado = parent.getItemAtPosition(position).toString()
                if (seleccionado == "añadir nuevo contexto") {
                    // Aquí puedes manejar la acción de añadir un nuevo contexto
                } else {
                    // Aquí puedes manejar la selección de un contexto
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
    fun onStarClick(view: View) { //Descomentar cuando este hecho lo del booleano

        // val starButton = view as ImageButton
        //val isStarred = // obtén el valor booleano de alguna parte

        //val color = if (isStarred) Color.YELLOW else Color.GRAY
        //starButton.setColorFilter(color)
    }
}