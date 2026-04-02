package com.example.khalinimaltaapp.model

import java.util.UUID // Não esqueça deste import!

// Modelo para gerenciar o acesso e a criação de senha
data class Credenciais(
    // Identificador único universal para a conta do usuário
    val id: String = UUID.randomUUID().toString(),
    val email: String = "",           // E-mail do usuário (geralmente o login)
    val senhaHash: String = "",       // A senha (que o João vai criptografar)
    val ultimaAlteracao: Long = System.currentTimeMillis(), // Quando a senha foi criada
    val tentativaLogin: Int = 0       // Para bloquear após 3 erros, por exemplo
)