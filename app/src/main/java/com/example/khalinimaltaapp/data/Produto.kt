package com.example.khalinimaltaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nomeProduto: String = "",
    val marca: String = "",
    val material: String = "",
    val codigoInterno: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val qtdeEstoque: Int = 0,
    val preco: Double = 0.0,
    val imagemUri: String? = null // Usamos String? para aceitar produtos sem foto e "Uri" por ser caminho local
)