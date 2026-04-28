package com.example.khalinimaltaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos") // Isso avisa ao Room que é uma tabela
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // O banco gera o ID automático (1, 2, 3...)
    val nomeProduto: String = "",
    val marca: String = "",
    val material: String = "",
    val codigoInterno: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val qtdeEstoque: Int = 0,
    val preco: Double = 0.0,
    val imagemUrl: String = ""
)