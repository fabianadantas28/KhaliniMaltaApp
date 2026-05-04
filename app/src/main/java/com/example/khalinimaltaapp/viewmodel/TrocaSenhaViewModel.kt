package com.example.khalinimaltaapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrocaSenhaViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()

    var novaSenha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")
    var mensagemErro by mutableStateOf("")
    var trocouComSucesso by mutableStateOf(false)

    fun trocarSenha(usuarioId: Int) {
        if (novaSenha.isBlank() || confirmarSenha.isBlank()) {
            mensagemErro = "Preencha todos os campos."
            return
        }
        if (novaSenha.length < 6) {
            mensagemErro = "A senha deve ter ao menos 6 caracteres."
            return
        }
        if (novaSenha != confirmarSenha) {
            mensagemErro = "As senhas não coincidem."
            return
        }

        mensagemErro = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                usuarioDao.atualizarSenha(usuarioId, novaSenha)
                trocouComSucesso = true
            } catch (e: Exception) {
                mensagemErro = "Erro ao atualizar senha: ${e.message}"
            }
        }
    }
}
