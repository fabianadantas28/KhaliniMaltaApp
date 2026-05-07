package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import com.example.khalinimaltaapp.data.dao.VendaDao
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

    fun adicionarAoCarrinho(nome: String, preco: String, qtd: Int) {
        if (nome.isEmpty()) return

        val precoDouble = preco.toDoubleOrNull() ?: 0.0
        val listaAtual = _itensCarrinho.value.toMutableList()

        val itemExistente = listaAtual.find { it.nome == nome }

        if (itemExistente != null) {
            val index = listaAtual.indexOf(itemExistente)
            listaAtual[index] = itemExistente.copy(quantidade = itemExistente.quantidade + qtd)
        } else {
            listaAtual.add(ItemCarrinho(nome, precoDouble, qtd))
        }

        // ATUALIZA A LISTA MANTENDO OS ANTERIORES
        _itensCarrinho.value = listaAtual
    }

    fun finalizarCompra(formaPagamento: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            try {
                val lista = _itensCarrinho.value
                lista.forEach { item ->
                    val produtos = produtoDao.buscarProdutosPorNome(item.nome).first()
                    val p = produtos.firstOrNull()
                    if (p != null) {
                        produtoDao.atualizarEstoque(p.id, p.qtdeEstoque - item.quantidade)
                    }
                }
                _itensCarrinho.value = emptyList() // Limpa só depois de vender tudo
                onSucesso()
            } catch (e: Exception) {
                _erroVenda.value = "Erro: ${e.message}"
            }
        }
    }
}