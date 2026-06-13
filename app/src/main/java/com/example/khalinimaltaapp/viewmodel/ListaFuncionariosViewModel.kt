package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Usuario
import com.example.khalinimaltaapp.data.dao.UsuarioDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListaFuncionariosViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    // Puxa todos os funcionários/admins do banco em tempo real
    val funcionarios: StateFlow<List<Usuario>> = usuarioDao.listarTodosUsuarios().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Função para deletar o funcionário por ID
    fun excluirFuncionario(id: Int) {
        viewModelScope.launch {
            usuarioDao.deletarPorId(id)
        }
    }
}