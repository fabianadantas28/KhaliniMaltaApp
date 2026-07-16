package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Cliente
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.Venda
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

// Convertido para ViewModel comum (Firebase dispensa o contexto de application aqui)
class RelatoriosViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // --- 1. CAPTAÇÃO DE DADOS EM TEMPO REAL DIRETAMENTE DA NUVEM ---

    val produtos: StateFlow<List<Produto>> = firestore.collection("produtos")
        .snapshots()
        .map { querySnapshot -> querySnapshot.toObjects(Produto::class.java) }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val clientes: StateFlow<List<Cliente>> = firestore.collection("clientes")
        .snapshots()
        .map { querySnapshot -> querySnapshot.toObjects(Cliente::class.java) }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vendas: StateFlow<List<Venda>> = firestore.collection("vendas")
        .snapshots()
        .map { querySnapshot -> querySnapshot.toObjects(Venda::class.java) }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- 2. MÉTRICAS DE ESTOQUE E CLIENTES ---

    val totalProdutos: StateFlow<Int> = produtos
        .map { it.size }
        .flowOn(Dispatchers.Default)
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