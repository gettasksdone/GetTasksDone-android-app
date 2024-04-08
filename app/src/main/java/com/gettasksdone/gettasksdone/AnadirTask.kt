package com.gettasksdone.gettasksdone

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
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
    }
    fun onStarClick(view: View) { //Descomentar cuando este hecho lo del booleano

        // val starButton = view as ImageButton
        //val isStarred = // obtén el valor booleano de alguna parte

        //val color = if (isStarred) Color.YELLOW else Color.GRAY
        //starButton.setColorFilter(color)
    }
}