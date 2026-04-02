package com.example.khalinimaltaapp.model

import java.util.UUID // Importação necessária

data class Venda(
    // Aqui geramos um ID único automaticamente toda vez que uma venda é criada
    val id: String = UUID.randomUUID().toString(),
    val produto: String = "",
    val cliente: String = "",
    val valor: Double = 0.0,
    val dataVenda: Long = System.currentTimeMillis()
)