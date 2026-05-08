package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CadastroProdutoViewModel(private val produtoDao: ProdutoDao) : ViewModel() {

    // --- 1. ESTADOS PARA O FORMULÁRIO DE CADASTRO (O QUE VOCÊ DIGITA) ---
    var nome by mutableStateOf("")
    var marca by mutableStateOf("")
    var material by mutableStateOf("")
    var codigo by mutableStateOf("")
    var preco by mutableStateOf("")
    var descricao by mutableStateOf("")
    var categoria by mutableStateOf("")
    var estoque by mutableStateOf("")

    // --- 2. FUNÇÃO PARA SALVAR NOVO PRODUTO (ADMINISTRAÇÃO) ---
    fun salvarProduto(onSucesso: () -> Unit, onError: (String) -> Unit) {
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
                    imagemUrl = ""
                )

                produtoDao.inserir(novoProduto)
                limparCampos()
                onSucesso()
            } catch (e: Exception) {
                onError("Erro ao salvar: ${e.message}")
            }
        }
    }

    // --- 3. FUNÇÃO PARA A VITRINE (BUSCAR PRODUTOS PARA O CLIENTE) ---
    // Esta função "conversa" com o DAO para trazer a lista filtrada
    // No seu CadastroProdutoViewModel.kt, altere apenas esta parte:

    // Antes era: fun getProdutosPorCategoria
    // Agora coloque o nome que o João sugeriu para padronizar:
    fun buscarPorCategoria(categoriaNome: String): Flow<List<Produto>> {
        return produtoDao.buscarPorCategoria(categoriaNome)
    }

    // --- 4. FUNÇÃO DE VENDA (DAR BAIXA NO ESTOQUE) ---
    fun venderProduto(produto: Produto) {
        if (produto.qtdeEstoque > 0) {
            viewModelScope.launch {
                val produtoAtualizado = produto.copy(
                    qtdeEstoque = produto.qtdeEstoque - 1
                )
                // Alterado para updateProduto para alinhar com o DAO novo
                produtoDao.updateProduto(produtoAtualizado)
            }
        }
    }

    // Limpa os campos após o cadastro com sucesso
    private fun limparCampos() {
        nome = ""; marca = ""; material = ""; codigo = ""
        preco = ""; descricao = ""; categoria = ""; estoque = ""
    }
}