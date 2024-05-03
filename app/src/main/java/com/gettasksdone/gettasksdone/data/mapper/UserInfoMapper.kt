package com.gettasksdone.gettasksdone.data.mapper

import com.gettasksdone.gettasksdone.data.local.entities.UserInfoEntity
import com.gettasksdone.gettasksdone.model.UserInfo

fun UserInfo.toEntity(): UserInfoEntity{
    return UserInfoEntity(
        id = this.id,
        nombre = this.nombre,
        puesto = this.puesto,
        departamento = this.departamento,
        userId = this.usuarioId,
        telefono = this.telefono
    )
}

fun UserInfoEntity.toDomain(): UserInfo{
    return UserInfo(
        id = this.id,
        nombre = this.nombre,
        puesto = this.puesto,
        departamento = this.departamento,
        usuarioId = this.userId,
        telefono = this.telefono
    )
}