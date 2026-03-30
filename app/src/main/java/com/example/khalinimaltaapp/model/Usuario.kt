package com.example.khalinimaltaapp.model

data class Usuario(
    val id: String = java.util.UUID.randomUUID().toString(), // Gera um ID único automático
    val nomeUsuario: String = "",
    val login: String = "",
    val senha: String = "",
    val datahora: String = ""
)