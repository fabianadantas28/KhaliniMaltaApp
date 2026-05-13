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

    // --- 1. ESTADOS PARA O FORMULÁRIO ---
    var nome by mutableStateOf("")
    var marca by mutableStateOf("")
    var material by mutableStateOf("")
    var codigo by mutableStateOf("")
    var preco by mutableStateOf("")
    var descricao by mutableStateOf("")
    var categoria by mutableStateOf("")
    var estoque by mutableStateOf("")

    // NOVO: Estado para armazenar a URI da imagem selecionada
    var imagemUriState by mutableStateOf<String?>(null)

    // --- 2. FUNÇÃO PARA SALVAR NOVO PRODUTO ---
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
                    imagemUri = imagemUriState // AGORA SALVA A URI REAL
                )

                produtoDao.inserir(novoProduto)
                limparCampos()
                onSucesso()
            } catch (e: Exception) {
                onError("Erro ao salvar: ${e.message}")
            }
        }
    }

    // --- 3. FUNÇÃO PARA A VITRINE ---
    fun buscarPorCategoria(categoriaNome: String): Flow<List<Produto>> {
        return produtoDao.buscarPorCategoria(categoriaNome)
    }

    // --- 4. FUNÇÃO DE VENDA ---
    fun venderProduto(produto: Produto) {
        if (produto.qtdeEstoque > 0) {
            viewModelScope.launch {
                val produtoAtualizado = produto.copy(
                    qtdeEstoque = produto.qtdeEstoque - 1
                )
                produtoDao.updateProduto(produtoAtualizado)
            }
        }
    }

    // Limpa os campos após o cadastro com sucesso
    private fun limparCampos() {
        nome = ""; marca = ""; material = ""; codigo = ""
        preco = ""; descricao = ""; categoria = ""; estoque = ""
        imagemUriState = null // Limpa a foto também
    }
}