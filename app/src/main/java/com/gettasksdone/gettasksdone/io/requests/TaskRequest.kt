package com.gettasksdone.gettasksdone.io.requests

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date


data class TaskRequest(
    val descripcion: String,
    val creacion: String = getCurrentDate(),
    val vencimiento: String?,
    val estado: String,
    val prioridad: Int
)

@SuppressLint("SimpleDateFormat")
fun getCurrentDate(): String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val fecha = Date()
    return dateFormat.format(fecha)
}