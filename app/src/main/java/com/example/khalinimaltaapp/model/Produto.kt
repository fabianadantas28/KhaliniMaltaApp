package com.example.khalinimaltaapp.model

data class Produto(
    val id: String = java.util.UUID.randomUUID().toString(), // O "placa" do produto no sistema
    val nomeProduto: String = "",
    val marca: String = "",
    val material: String = "",
    val codigoInterno: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val qtdeEstoque: Int = 0, // Alterado para Int para podermos comparar,
    val preco: Double = 0.0,
    val imagemUrl: String = "" // Para a foto do produto que aparece no design
)