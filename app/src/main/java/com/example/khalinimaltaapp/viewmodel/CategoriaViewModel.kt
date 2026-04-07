package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.khalinimaltaapp.R // Importante para as imagens

// Definindo o que cada item da lista precisa ter
data class CategoriaItem(
    val nome: String,
    val imagemRes: Int,
    val rota: String
)

class CategoriaViewModel : ViewModel() {

    // Aqui é onde o "estoque" de categorias fica guardado
    private val _categorias = MutableStateFlow<List<CategoriaItem>>(emptyList())
    val categorias: StateFlow<List<CategoriaItem>> = _categorias

    init {
        carregarCategorias()
    }

    private fun carregarCategorias() {
        // Esta é a lista que o seu App vai ler
        _categorias.value = listOf(
            CategoriaItem("Anéis", R.drawable.anel, "aneis"),
            CategoriaItem("Colares", R.drawable.colar, "colares"),
            CategoriaItem("Brincos", R.drawable.brinco, "brincos"),
            CategoriaItem("Pulseiras", R.drawable.pulseira, "pulseiras"),
            CategoriaItem("Tornozeleiras", R.drawable.tornozeleira, "tornozeleiras"),
            CategoriaItem("Acessórios", R.drawable.acessorios, "acessorios")
        )
    }
}