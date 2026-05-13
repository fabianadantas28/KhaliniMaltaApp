package com.example.khalinimaltaapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.database.AppDatabase
import kotlinx.coroutines.flow.*

class RelatoriosViewModel(application: Application) : AndroidViewModel(application) {

    private val produtoDao = AppDatabase.getDatabase(application).produtoDao()
    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()
    private val vendaDao = AppDatabase.getDatabase(application).vendaDao()

    // --- 1. DADOS BRUTOS (Declarados primeiro para evitar erros de referência) ---

    val produtos = produtoDao.getAllProdutos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val clientes = clienteDao.getAllClientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vendas = vendaDao.listarTodasVendas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- 2. MÉTRICAS DE ESTOQUE E CLIENTES ---

    val totalProdutos: StateFlow<Int> = produtos
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalClientes: StateFlow<Int> = clientes
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val valorTotalEstoque: StateFlow<Double> = produtos
        .map { lista -> lista.sumOf { it.preco * it.qtdeEstoque } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val produtosAlerta: StateFlow<Int> = produtos
        .map { lista -> lista.count { it.qtdeEstoque in 1..5 } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalItensEstoque: StateFlow<Int> = produtos
        .map { lista -> lista.sumOf { it.qtdeEstoque } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val ticketMedio: StateFlow<Double> = produtos
        .map { lista ->
            if (lista.isEmpty()) 0.0
            else lista.sumOf { it.preco } / lista.size
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // --- 3. MÉTRICAS DE VENDAS ---

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

    // --- 4. GRÁFICOS (DIFERENCIADOS) ---

    // Gráfico Diário (Home) - Pega as últimas 7 vendas individuais para as barras
    val dadosGraficoDiario: StateFlow<List<Float>> = vendas
        .map { listaVendas ->
            val valores = MutableList(7) { 0.1f }
            listaVendas.takeLast(7).forEachIndexed { index, venda ->
                val altura = (venda.valorTotal / 500.0).toFloat().coerceIn(0.2f, 1.0f)
                if (index < 7) valores[index] = altura
            }
            valores
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), List(7) { 0.1f })

    // Gráfico Mensal (Relatórios) - Agrupa as vendas para mostrar o semestre
    val dadosGraficoMensal: StateFlow<List<Float>> = vendas
        .map { listaVendas ->
            val valores = MutableList(6) { 0.1f }
            if (listaVendas.isNotEmpty()) {
                // Simulação: agrupa vendas para preencher 6 meses
                listaVendas.takeLast(12).chunked(2).forEachIndexed { index, miniLista ->
                    if (index < 6) {
                        val soma = miniLista.sumOf { it.valorTotal }
                        valores[index] = (soma / 1000.0).toFloat().coerceIn(0.2f, 1.0f)
                    }
                }
            }
            valores
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), List(6) { 0.1f })
}