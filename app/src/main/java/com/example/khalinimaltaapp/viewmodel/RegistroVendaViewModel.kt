package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import com.example.khalinimaltaapp.data.dao.VendaDao
import com.example.khalinimaltaapp.data.Venda
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ItemCarrinho(
    val nome: String,
    val precoUnitario: Double,
    val quantidade: Int
)

class RegistroVendaViewModel(
    private val produtoDao: ProdutoDao,
    private val vendaDao: VendaDao
) : ViewModel() {

    private val _itensCarrinho = MutableStateFlow<List<ItemCarrinho>>(emptyList())
    val itensCarrinho = _itensCarrinho.asStateFlow()

    private val _erroVenda = MutableStateFlow("")
    val erroVenda = _erroVenda.asStateFlow()

    // --- LOGICA DO RECIBO ---
    // Esta variável guarda os dados que a tela ReciboScreen vai ler
    var vendaRealizadaParaRecibo by mutableStateOf<Venda?>(null)
        private set // Apenas o ViewModel pode alterar diretamente

    // Função para fechar o recibo e limpar os dados
    fun limparRecibo() {
        vendaRealizadaParaRecibo = null
    }

    fun adicionarAoCarrinho(nome: String, preco: String, qtd: Int) {
        if (nome.isEmpty()) return

        // CORREÇÃO: Limpa espaços e troca a vírgula por ponto para o Kotlin aceitar o número
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

    // Agora recebe nomeCliente e formaPagamento
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

                lista.forEach { item ->
                    // 1. Busca o produto para atualizar estoque
                    val produtosFlow = produtoDao.buscarProdutosPorNome(item.nome).first()
                    val p = produtosFlow.firstOrNull()

                    if (p != null) {
                        // 2. Atualiza o estoque no banco
                        val produtoComEstoqueAtualizado = p.copy(
                            qtdeEstoque = p.qtdeEstoque - item.quantidade
                        )
                        produtoDao.updateProduto(produtoComEstoqueAtualizado)

                        // 3. Registra a venda individual no banco de dados
                        val valorVendaItem = item.precoUnitario * item.quantidade
                        totalGeral += valorVendaItem
                        nomesDosProdutos.add("${item.quantidade}x ${item.nome}")

                        vendaDao.registrarVenda(
                            Venda(
                                produtoId = p.id,
                                nomeProduto = p.nomeProduto,
                                nomeCliente = if (nomeCliente.isBlank()) "Cliente Balcão" else nomeCliente,
                                telefoneCliente = "",
                                formaPagamento = formaPagamento,
                                quantidade = item.quantidade,
                                valorTotal = valorVendaItem,
                                dataHora = agora
                            )
                        )
                    }
                }

                // 4. PREPARA O RECIBO: Criamos um resumo de todos os itens para a tela de Recibo
                vendaRealizadaParaRecibo = Venda(
                    produtoId = 0,
                    nomeProduto = nomesDosProdutos.joinToString("\n"), // Lista todos os produtos comprados
                    nomeCliente = if (nomeCliente.isBlank()) "Cliente Balcão" else nomeCliente,
                    telefoneCliente = "",
                    formaPagamento = formaPagamento,
                    quantidade = lista.sumOf { it.quantidade },
                    valorTotal = totalGeral,
                    dataHora = agora
                )

                _itensCarrinho.value = emptyList() // Limpa o carrinho após o sucesso
                onSucesso()

            } catch (e: Exception) {
                _erroVenda.value = "Erro ao registrar venda: ${e.message}"
            }
        }
    }
}