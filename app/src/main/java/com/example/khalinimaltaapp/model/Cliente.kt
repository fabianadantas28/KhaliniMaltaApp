package com.example.khalinimaltaapp.model

data class Cliente(
    val nome: String = "",
    val sobrenome: String = "",
    val dataNasc: String = "",
    val cpf: String = "",
    val foneCelular: String = "", // Já ajustado conforme sua mudança!
    val email: String = "",
    val complemento: String = ""
)