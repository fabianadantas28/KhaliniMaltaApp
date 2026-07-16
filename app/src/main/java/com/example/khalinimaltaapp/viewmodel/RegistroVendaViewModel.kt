package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.Venda
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ItemCarrinho(
    val nome: String,
    val precoUnitario: Double,
    val quantidade: Int
)

// Removemos 'produtoDao' e 'vendaDao' do construtor
class RegistroVendaViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

    private val _itensCarrinho = MutableStateFlow<List<ItemCarrinho>>(emptyList())
    val itensCarrinho = _itensCarrinho.asStateFlow()

    private val _erroVenda = MutableStateFlow("")
    val erroVenda = _erroVenda.asStateFlow()

    // --- LOGICA DO RECIBO ---
    var vendaRealizadaParaRecibo by mutableStateOf<Venda?>(null)
        private set

    // Função para fechar o recibo e limpar os dados
    fun limparRecibo() {
        vendaRealizadaParaRecibo = null
    }

    fun adicionarAoCarrinho(nome: String, preco: String, qtd: Int) {
        if (nome.isEmpty()) return

        val precoLimpo = preco.trim().replace(",", ".")
        val precoDouble = precoLimpo.toDoubleOrNull() ?: 0.0

        val listaAtual = _itensCarrinho.value.toMutableList()
        val itemExistente = listaAtual.find { it.nome == nome }

        if (itemExistente != null) {
            val index = listaAtual.indexOf(itemExistente)
            listaAtual[index] = itemExistente.copy(quantidade = itemExistente.quantidade + qtd)
        } else {
            listaAtual.add(ItemCarrinho(nome, precoDouble, qtd))
        }

        _itensCarrinho.value = listaAtual
    }

    fun finalizarCompra(nomeCliente: String, formaPagamento: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            try {
                val lista = _itensCarrinho.value
                if (lista.isEmpty()) {
                    _erroVenda.value = "O carrinho está vazio!"
                    return@launch
                }

                val agora = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())

                var totalGeral = 0.0
                val nomesDosProdutos = mutableListOf<String>()

                // Iteramos pelos itens do carrinho usando co-rotinas com Firebase de forma sequencial ou paralela
                lista.forEach { item ->
                    // 1. Busca o produto na coleção "produtos" pelo nome para descobrir o ID / Código Interno e estoque
                    val queryProduto = firestore.collection("produtos")
                        .whereEqualTo("nomeProduto", item.nome)
                        .get()
                        .await()

                    val documentoProduto = queryProduto.documents.firstOrNull()
                    if (documentoProduto != null) {
                        val produtoNaNuvem = documentoProduto.toObject(Produto::class.java)

                        if (produtoNaNuvem != null) {
                            // 2. Atualiza o estoque diretamente na nuvem
                            val novoEstoque = produtoNaNuvem.qtdeEstoque - item.quantidade
                            documentoProduto.reference.update("qtdeEstoque", novoEstoque).await()

                            // 3. Soma os valores financeiros
                            val valorVendaItem = item.precoUnitario * item.quantidade
                            totalGeral += valorVendaItem
                            nomesDosProdutos.add("${item.quantidade}x ${item.nome}")

                            // 4. Registra o documento da venda na coleção "vendas" do Firestore
                            val novaVenda = Venda(
                                produtoId = 0, // Como o Firestore usa IDs em string ou gerados, o ID numérico vira opcional
                                nomeProduto = produtoNaNuvem.nomeProduto,
                                nomeCliente = if (nomeCliente.isBlank()) "Cliente Balcão" else nomeCliente,
                                telefoneCliente = "",
                                formaPagamento = formaPagamento,
                                quantidade = item.quantidade,
                                valorTotal = valorVendaItem,
                                dataHora = agora
                            )

                            firestore.collection("vendas")
                                .add(novaVenda)
                                .await()
                        }
                    }
                }

                // 5. PREPARA O RECIBO NA TELA
                vendaRealizadaParaRecibo = Venda(
                    produtoId = 0,
                    nomeProduto = nomesDosProdutos.joinToString("\n"),
                    nomeCliente = if (nomeCliente.isBlank()) "Cliente Balcão" else nomeCliente,
                    telefoneCliente = "",
                    formaPagamento = formaPagamento,
                    quantidade = lista.sumOf { it.quantidade },
                    valorTotal = totalGeral,
                    dataHora = agora
                )

                _itensCarrinho.value = emptyList() // Limpa o carrinho
                onSucesso()

            } catch (e: Exception) {
                _erroVenda.value = "Erro ao registrar venda: ${e.message}"
            }
        }
    }
}