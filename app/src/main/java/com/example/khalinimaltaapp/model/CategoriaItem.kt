package com.example.khalinimaltaapp.model

import java.util.UUID

data class CategoriaItem(
    val id: String = UUID.randomUUID().toString(), // Igual à "placa" que você usou no Produto
    val nome: String = "",
    val imagemRes: Int = 0,
    val rota: String = ""
)