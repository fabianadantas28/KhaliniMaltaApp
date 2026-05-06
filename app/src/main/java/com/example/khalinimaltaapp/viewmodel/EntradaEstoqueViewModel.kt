package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EntradaEstoqueViewModel(private val produtoDao: ProdutoDao) : ViewModel() {

    var buscaTexto by mutableStateOf("")
    var quantidadeAdicional by mutableStateOf("")
    var produtoSelecionado by mutableStateOf<Produto?>(null)

    private val _produtosEncontrados = MutableStateFlow<List<Produto>>(emptyList())
    val produtosEncontrados = _produtosEncontrados.asStateFlow()

    fun buscarProduto(nome: String) {
        buscaTexto = nome
        viewModelScope.launch {
            // Aqui chamamos a função EXATA que criamos no DAO acima
            produtoDao.buscarProdutosPorNome(nome).collect { lista ->
                _produtosEncontrados.value = lista
            }
        }
    }

    fun confirmarEntrada(onSucesso: () -> Unit) {
        val produto = produtoSelecionado ?: return
        val adicional = quantidadeAdicional.toIntOrNull() ?: 0

        if (adicional > 0) {
            viewModelScope.launch {
                val novaQtde = produto.qtdeEstoque + adicional
                // Chamamos a função EXATA que criamos no DAO
                produtoDao.atualizarEstoque(produto.id, novaQtde)
                onSucesso()
            }
        }
    }
}