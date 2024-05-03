package com.gettasksdone.gettasksdone.data.mapper

import com.gettasksdone.gettasksdone.data.local.entities.UserEntity
import com.gettasksdone.gettasksdone.model.User

fun User.toEntity(): UserEntity{
    return UserEntity(
        id = this.id,
        username = this.username,
        email = this.email,
        rol = this.rol
    )
}

fun UserEntity.toDomain(): User{
    return User(
        id = this.id,
        username = this.username,
        email = this.email,
        rol = this.rol
    )
}