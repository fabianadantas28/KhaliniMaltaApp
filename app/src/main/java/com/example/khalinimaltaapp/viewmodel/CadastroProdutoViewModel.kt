package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import kotlinx.coroutines.launch

class CadastroProdutoViewModel(private val produtoDao: ProdutoDao) : ViewModel() {

    // 1. Variáveis para segurar o que o usuário digita (Estado)
    var nomeProduto by mutableStateOf("")
    var descricao by mutableStateOf("")
    var preco by mutableStateOf("") // Adicionamos o preço aqui!
    var categoria by mutableStateOf("")
    var imagemUrl by mutableStateOf("")

    // 2. Função para salvar no banco de dados
    fun salvarProduto(onSucesso: () -> Unit) {
        viewModelScope.launch {
            val novoProduto = Produto(
                nomeProduto = nomeProduto, // Ajustado para o nome correto do parâmetro
                descricao = descricao,
                preco = preco.toDoubleOrNull() ?: 0.0,
                categoria = categoria,
                imagemUrl = imagemUrl
            )
            produtoDao.inserir(novoProduto)
            limparCampos()
            onSucesso()
        }

    }

    private fun limparCampos() {
        nomeProduto = ""
        descricao = ""
        preco = ""
        categoria = ""
        imagemUrl = ""
    }
}