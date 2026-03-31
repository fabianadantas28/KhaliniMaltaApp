package com.example.khalinimaltaapp.model

data class Cliente(
    val id: String = java.util.UUID.randomUUID().toString(),
    val nome: String = "",
    val sobrenome: String = "",
    val dataNasc: String = "",
    val cpf: String = "",
    val foneCelular: String = "",
    val email: String = "",
    val complemento: String = "",
    val consentimentoLGPD: Boolean = false, // false significa que começa desmarcado
    val dataConsentimento: String = "" // É bom guardar QUANDO o cliente aceitou, como no desenho!
)