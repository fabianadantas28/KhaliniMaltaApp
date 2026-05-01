package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroVendaViewModel(private val produtoDao: ProdutoDao) : ViewModel() {

    private val _produtosEncontrados = MutableStateFlow<List<Produto>>(emptyList())
    val produtosEncontrados = _produtosEncontrados.asStateFlow()

    fun buscarProduto(query: String) {
        viewModelScope.launch {
            if (query.length > 2) {
                // Aqui você pode criar uma busca específica no DAO depois
                // Por enquanto, vamos filtrar da lista total
                produtoDao.getAllProdutos().collect { lista ->
                    _produtosEncontrados.value = lista.filter {
                        it.nomeProduto.contains(query, ignoreCase = true) ||
                                it.codigoInterno.contains(query)
                    }
                }
            }
        }
    }

    // Adicione estes estados no seu RegistroVendaViewModel
    private val _clientesEncontrados = MutableStateFlow<List<com.example.khalinimaltaapp.data.Cliente>>(emptyList())
    val clientesEncontrados = _clientesEncontrados.asStateFlow()

    // Adicione esta função (assumindo que você tenha um clienteDao)
    fun buscarCliente(query: String, clienteDao: com.example.khalinimaltaapp.data.dao.ClienteDao) {
        viewModelScope.launch {
            if (query.length >= 2) {
                clienteDao.getAllClientes().collect { lista ->
                    _clientesEncontrados.value = lista.filter {
                        it.nome.contains(query, ignoreCase = true)
                    }
                }
            } else {
                _clientesEncontrados.value = emptyList()
            }
        }
    }

    fun confirmarVenda(produto: Produto, quantidade: Int) {
        viewModelScope.launch {
            val novoEstoque = produto.qtdeEstoque - quantidade
            produtoDao.updateProduto(produto.copy(qtdeEstoque = novoEstoque))
        }
    }
}