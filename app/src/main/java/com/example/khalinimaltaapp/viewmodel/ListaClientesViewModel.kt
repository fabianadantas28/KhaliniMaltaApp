package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Cliente
import com.example.khalinimaltaapp.data.dao.ClienteDao
import kotlinx.coroutines.flow.*

class ListaClientesViewModel(private val clienteDao: ClienteDao) : ViewModel() {

    // 1. Usamos StateFlow para a busca para que o 'combine' perceba a mudança
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 2. Fluxo original do banco
    private val _clientesBase = clienteDao.getAllClientes()

    // 3. O combine agora observa o _searchQuery corretamente
    val clientes: StateFlow<List<Cliente>> = _clientesBase
        .combine(_searchQuery) { lista, query ->
            if (query.isEmpty()) {
                lista
            } else {
                lista.filter { cliente ->
                    cliente.nome.contains(query, ignoreCase = true) ||
                            cliente.sobrenome.contains(query, ignoreCase = true) ||
                            cliente.cpf.contains(query)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Função para atualizar a busca
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
}