package com.example.khalinimaltaapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class RelatoriosViewModel(application: Application) : AndroidViewModel(application) {

    private val produtoDao = AppDatabase.getDatabase(application).produtoDao()
    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()
    private val vendaDao = AppDatabase.getDatabase(application).vendaDao()

    // --- 1. DADOS BRUTOS ASSEGURADOS EM SEGUNDO PLANO (AQUI ESTÁ A CORREÇÃO CRÍTICA) ---

    val produtos = produtoDao.getAllProdutos()
        .flowOn(Dispatchers.IO) // Move a busca do banco para a Thread correta de IO
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val clientes = clienteDao.getAllClientes()
        .flowOn(Dispatchers.IO) // Move a busca do banco para a Thread correta de IO
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vendas = vendaDao.listarTodasVendas()
        .flowOn(Dispatchers.IO) // Move a busca do banco para a Thread correta de IO
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- 2. MÉTRICAS DE ESTOQUE E CLIENTES ---

    val totalProdutos: StateFlow<Int> = produtos
        .map { it.size }
        .flowOn(Dispatchers.Default) // Cálculos leves processados de forma assíncrona
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalClientes: StateFlow<Int> = clientes
        .map { it.size }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val valorTotalEstoque: StateFlow<Double> = produtos
        .map { lista -> lista.sumOf { it.preco * it.qtdeEstoque } }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val produtosAlerta: StateFlow<Int> = produtos
        .map { lista -> lista.count { it.qtdeEstoque in 1..5 } }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalItensEstoque: StateFlow<Int> = produtos
        .map { lista -> lista.sumOf { it.qtdeEstoque } }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val ticketMedio: StateFlow<Double> = produtos
        .map { lista ->
            if (lista.isEmpty()) 0.0
            else lista.sumOf { it.preco } / lista.size
        }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // --- 3. MÉTRICAS DE VENDAS ---

    val totalVendas: StateFlow<Int> = vendas
        .map { it.size }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val receitaTotal: StateFlow<Double> = vendas
        .map { lista -> lista.sumOf { it.valorTotal } }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val produtoMaisVendido: StateFlow<String> = vendas
        .map { lista ->
            if (lista.isEmpty()) "—"
            else lista.groupBy { it.nomeProduto }
                .maxByOrNull { it.value.sumOf { v -> v.quantidade } }?.key ?: "—"
        }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "—")

    val formaPagamentoMaisUsada: StateFlow<String> = vendas
        .map { lista ->
            if (lista.isEmpty()) "—"
            else lista.groupBy { it.formaPagamento }
                .maxByOrNull { it.value.size }?.key ?: "—"
        }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "—")

    // --- 4. GRÁFICOS PROTEGIDOS CONTRA CRASH E TRAVAMENTO ---

    val dadosGraficoDiario: StateFlow<List<Float>> = vendas
        .map { listaVendas ->
            val valores = MutableList(7) { 0.1f }
            if (listaVendas.isNotEmpty()) {
                val ultimasVendas = listaVendas.takeLast(7)
                ultimasVendas.forEachIndexed { index, venda ->
                    val altura = (venda.valorTotal / 500.0).toFloat().coerceIn(0.2f, 1.0f)
                    if (index in 0..6) {
                        valores[index] = altura
                    }
                }
            }
            valores
        }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), List(7) { 0.1f })

    val dadosGraficoMensal: StateFlow<List<Float>> = vendas
        .map { listaVendas ->
            val valores = MutableList(6) { 0.1f }
            if (listaVendas.isNotEmpty()) {
                val ultimasDoze = listaVendas.takeLast(12)
                ultimasDoze.chunked(2).forEachIndexed { index, miniLista ->
                    if (index in 0..5) {
                        val soma = miniLista.sumOf { it.valorTotal }
                        valores[index] = (soma / 1000.0).toFloat().coerceIn(0.2f, 1.0f)
                    }
                }
            }
            valores
        }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), List(6) { 0.1f })
}