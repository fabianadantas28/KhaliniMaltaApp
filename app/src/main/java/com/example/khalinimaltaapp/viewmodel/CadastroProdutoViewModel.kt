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

    // Estados que a Screen vai observar
    var nome by mutableStateOf("")
    var marca by mutableStateOf("")
    var material by mutableStateOf("")
    var codigo by mutableStateOf("")
    var preco by mutableStateOf("")
    var descricao by mutableStateOf("")
    var categoria by mutableStateOf("")
    var estoque by mutableStateOf("")

    fun salvarProduto(onSucesso: () -> Unit, onError: (String) -> Unit) {
        // Validação básica
        if (nome.isBlank() || categoria.isBlank() || preco.isBlank()) {
            onError("Preencha Nome, Categoria e Preço!")
            return
        }

        viewModelScope.launch {
            try {
                val novoProduto = Produto(
                    nomeProduto = nome,
                    marca = marca,
                    material = material,
                    codigoInterno = codigo,
                    descricao = descricao,
                    categoria = categoria,
                    qtdeEstoque = estoque.toIntOrNull() ?: 0,
                    preco = preco.replace(",", ".").toDoubleOrNull() ?: 0.0,
                    imagemUrl = "" // Campo obrigatório na Entity
                )

                produtoDao.inserir(novoProduto)
                limparCampos()
                onSucesso()
            } catch (e: Exception) {
                onError("Erro ao salvar: ${e.message}")
            }
        }
    }

    private fun limparCampos() {
        nome = ""; marca = ""; material = ""; codigo = ""
        preco = ""; descricao = ""; categoria = ""; estoque = ""
    }
}