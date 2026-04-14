package com.example.khalinimaltaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "clientes")
data class Cliente(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String,
    val sobrenome: String,
    val cpf: String,
    val telefone: String,
    val email: String,
    val data: String,
    val senha: String
)