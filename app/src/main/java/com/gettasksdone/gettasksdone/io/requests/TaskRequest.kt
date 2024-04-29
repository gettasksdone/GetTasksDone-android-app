package com.gettasksdone.gettasksdone.io.requests

import android.annotation.SuppressLint
import com.gettasksdone.gettasksdone.model.Context
import java.text.SimpleDateFormat
import java.util.Date


data class TaskRequest(
    val titulo: String,
    val descripcion: String? = null,
    val creacion: String = getCurrentDate(),
    val vencimiento: String?,
    val estado: String,
    val prioridad: Int,
    val contexto: Context
)

@SuppressLint("SimpleDateFormat")
fun getCurrentDate(): String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val fecha = Date()
    return dateFormat.format(fecha)
}