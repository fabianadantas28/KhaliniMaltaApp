package com.example.khalinimaltaapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.database.AppDatabase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RelatoriosViewModel(application: Application) : AndroidViewModel(application) {

    private val produtoDao = AppDatabase.getDatabase(application).produtoDao()
    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()
    private val vendaDao = AppDatabase.getDatabase(application).vendaDao()

    // --- Dados brutos do banco ---
    val produtos = produtoDao.getAllProdutos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val clientes = clienteDao.getAllClientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Métricas calculadas ---

    // Total de produtos cadastrados
    val totalProdutos: StateFlow<Int> = produtos
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Total de clientes cadastrados
    val totalClientes: StateFlow<Int> = clientes
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Valor total do estoque (preço × quantidade de cada produto)
    val valorTotalEstoque: StateFlow<Double> = produtos
        .map { lista -> lista.sumOf { it.preco * it.qtdeEstoque } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Total de itens em estoque (soma das quantidades)
    val totalItensEstoque: StateFlow<Int> = produtos
        .map { lista -> lista.sumOf { it.qtdeEstoque } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Ticket médio (valor total / total de produtos, evita divisão por zero)
    val ticketMedio: StateFlow<Double> = produtos
        .map { lista ->
            if (lista.isEmpty()) 0.0
            else lista.sumOf { it.preco } / lista.size
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    // --- Métricas de Vendas (Novidade!) ---

    val vendas = vendaDao.listarTodasVendas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalVendas: StateFlow<Int> = vendas
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val receitaTotal: StateFlow<Double> = vendas
        .map { lista -> lista.sumOf { it.valorTotal } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val produtoMaisVendido: StateFlow<String> = vendas
        .map { lista ->
            if (lista.isEmpty()) "—"
            else lista.groupBy { it.nomeProduto }
                .maxByOrNull { it.value.sumOf { v -> v.quantidade } }?.key ?: "—"
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "—")

    val formaPagamentoMaisUsada: StateFlow<String> = vendas
        .map { lista ->
            if (lista.isEmpty()) "—"
            else lista.groupBy { it.formaPagamento }
                .maxByOrNull { it.value.size }?.key ?: "—"
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "—")
}

