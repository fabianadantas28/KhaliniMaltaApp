package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import com.example.khalinimaltaapp.data.Produto
import kotlinx.coroutines.flow.*
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ControleEstoqueViewModel(private val produtoDao: ProdutoDao) : ViewModel() {

    // 1. Variável que guarda o texto da busca (vinculada ao TextField da tela)
    var buscaTexto by mutableStateOf("")

    // 2. O "Motor de Busca": ele observa a buscaTexto e decide qual lista trazer
    val produtosFiltrados: StateFlow<List<Produto>> = snapshotFlow { buscaTexto }
        .debounce(300) // Espera 300ms após o usuário parar de digitar para buscar (economiza processamento)
        .flatMapLatest { texto ->
            if (texto.isBlank()) {
                produtoDao.getAllProdutos() // Se vazio, mostra tudo
            } else {
                produtoDao.buscarProdutosPorNome(texto) // Se digitou, filtra pelo nome
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}