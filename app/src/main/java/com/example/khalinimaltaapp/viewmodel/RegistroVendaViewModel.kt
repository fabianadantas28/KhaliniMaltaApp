package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import com.example.khalinimaltaapp.data.dao.VendaDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegistroVendaViewModel(
    private val produtoDao: ProdutoDao,
    private val vendaDao: VendaDao
) : ViewModel() {

    // 1. Lista de produtos encontrados na busca
    private val _produtosEncontrados = MutableStateFlow<List<Produto>>(emptyList())
    val produtosEncontrados = _produtosEncontrados.asStateFlow()

    // 2. Estado para avisar a tela que a venda deu certo
    private val _vendaConfirmada = MutableStateFlow(false)
    val vendaConfirmada = _vendaConfirmada.asStateFlow()

    // 3. Estado para mensagens de erro
    private val _erroVenda = MutableStateFlow("")
    val erroVenda = _erroVenda.asStateFlow()

    // Função de busca que a sua Screen chama no 'onValueChange'
    // Dentro do seu RegistroVendaViewModel
    fun buscarProduto(query: String) {
        viewModelScope.launch {
            // Agora o 'buscarPorNome' vai ser reconhecido!
            produtoDao.buscarProdutosPorNome(query).collect { lista ->
                _produtosEncontrados.value = lista
            }
        }
    }

    fun confirmarVenda(produto: Produto, quantidade: Int) {
        viewModelScope.launch {
            if (produto.qtdeEstoque >= quantidade) {
                // Usando 'atualizar' que é o nome real no seu DAO
                produtoDao.atualizar(produto.copy(qtdeEstoque = produto.qtdeEstoque - quantidade))
                _vendaConfirmada.value = true
            }
        }
    }

    // Função que o botão "CONFIRMAR VENDA" da sua tela chama
    fun confirmarVenda(
        produto: Produto,
        quantidade: Int,
        nomeCliente: String,
        telefoneCliente: String,
        formaPagamento: String
    ) {
        viewModelScope.launch {
            try {
                // Valida estoque usando o nome correto: qtdeEstoque
                if (produto.qtdeEstoque < quantidade) {
                    _erroVenda.value = "Estoque insuficiente!"
                    return@launch
                }

                // Atualiza o estoque no banco
                val produtoAtualizado = produto.copy(qtdeEstoque = produto.qtdeEstoque - quantidade)
                produtoDao.atualizar(produtoAtualizado)

                // Se chegou aqui, deu certo
                _vendaConfirmada.value = true
                _erroVenda.value = ""
            } catch (e: Exception) {
                _erroVenda.value = "Erro: ${e.message}"
            }
        }
    }
}