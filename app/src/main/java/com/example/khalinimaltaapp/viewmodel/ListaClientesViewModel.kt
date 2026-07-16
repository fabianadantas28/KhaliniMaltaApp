package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Cliente
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
// Removemos o 'clienteDao' do construtor
class ListaClientesViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

    // 1. Guarda o termo digitado na barra de pesquisa
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 2. O motor reativo: escuta a nuvem e reage à alteração da barra de pesquisa
    val clientes: StateFlow<List<Cliente>> = _searchQuery
        .flatMapLatest { query ->
            val colecao = firestore.collection("clientes")

            val firestoreQuery = if (query.isBlank()) {
                colecao // Se vazio, pega a lista completa
            } else {
                // Filtra clientes cujo nome começa com o termo pesquisado
                colecao.whereGreaterThanOrEqualTo("nome", query)
                    .whereLessThanOrEqualTo("nome", query + "\uf8ff")
            }

            firestoreQuery.snapshots().map { querySnapshot ->
                querySnapshot.toObjects(Cliente::class.java)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Função para atualizar o termo da busca
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
}