package com.example.khalinimaltaapp.model

data class LogAcesso(
    val id: String = java.util.UUID.randomUUID().toString(),
    val usuario: String = "", // Quem tentou entrar
    val acao: String = "",    // O que ele fez (ex: "Entrou no sistema")
    val dataHora: String = "" // Quando foi
)
