package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
// Removemos o 'produtoDao' do construtor
class ControleEstoqueViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

    // 1. Variável que guarda o texto da busca (vinculada ao TextField da tela)
    var buscaTexto by mutableStateOf("")

    // 2. O "Motor de Busca" na nuvem: observa a buscaTexto em tempo real
    val produtosFiltrados: StateFlow<List<Produto>> = snapshotFlow { buscaTexto }
        .debounce(300) // Mantém a pausa inteligente de 300ms para poupar internet e processamento
        .flatMapLatest { texto ->
            val colecao = firestore.collection("produtos")

            val query = if (texto.isBlank()) {
                colecao // Se vazio, traz a referência completa da coleção
            } else {
                // Filtra no Firestore por produtos que começam com o texto digitado
                colecao.whereGreaterThanOrEqualTo("nomeProduto", texto)
                    .whereLessThanOrEqualTo("nomeProduto", texto + "\uf8ff")
            }

            query.snapshots().map { querySnapshot ->
                querySnapshot.toObjects(Produto::class.java)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}