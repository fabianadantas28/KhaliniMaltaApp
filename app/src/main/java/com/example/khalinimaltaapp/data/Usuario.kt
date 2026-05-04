package com.example.khalinimaltaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) // Deixe o Room gerar o número 1, 2, 3...
    val id: Int = 0,
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val datahora: String = "",
    val perfil: String = "FUNCIONARIO",  // "ADMIN" ou "FUNCIONARIO"
    val trocarSenha: Boolean = true       // true = primeiro acesso
)

