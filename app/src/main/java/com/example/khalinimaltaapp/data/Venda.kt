package com.example.khalinimaltaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vendas")
data class Venda(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val produtoId: Int,
    val nomeProduto: String,
    val nomeCliente: String,
    val telefoneCliente: String,
    val formaPagamento: String,
    val quantidade: Int,
    val valorTotal: Double,
    val dataHora: String
)
