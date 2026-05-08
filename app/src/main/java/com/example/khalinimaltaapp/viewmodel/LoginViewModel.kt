package com.example.khalinimaltaapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Usuario
import com.example.khalinimaltaapp.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()
    private val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()

    var usuario by mutableStateOf("")
    var senha by mutableStateOf("")
    var mostrarDialogo by mutableStateOf(false)
    var loginErro by mutableStateOf(false)

    var irParaHome by mutableStateOf(false)
    var irParaTrocaSenha by mutableStateOf(false)
    var tipoUsuarioLogado by mutableStateOf("")

    var usuarioLogado: Usuario? = null

    // ALTERAÇÃO AQUI: A função agora pede o sharedViewModel para salvar o nome do cliente
    fun fazerLogin(sharedViewModel: CadastroClienteViewModel) {
        if (usuario.isBlank() || senha.isBlank()) {
            loginErro = true
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val usuarioDigitado = usuario.trim().lowercase()

            // 1. LOGIN ADMIN (Bloco de Emergência)
            if (usuarioDigitado == "admin" && senha == "admin") {
                tipoUsuarioLogado = "ADMIN"
                sharedViewModel.nome = "Administrador" // Define um nome para o admin
                irParaHome = true
                loginErro = false
                return@launch
            }

            // 2. LOGIN FUNCIONÁRIO/ADMIN CADASTRADO
            val func = usuarioDao.realizarLogin(usuarioDigitado, senha)
            if (func != null) {
                usuarioLogado = func
                tipoUsuarioLogado = func.perfil

                // SALVA O NOME DO FUNCIONÁRIO NO VIEWMODEL COMPARTILHADO
                sharedViewModel.nome = func.nome

                if (func.trocarSenha) {
                    irParaTrocaSenha = true
                } else {
                    irParaHome = true
                }
                loginErro = false
                return@launch
            }

            // 3. LOGIN CLIENTE
            val cliente = clienteDao.buscarPorEmailESenha(usuarioDigitado, senha)
            if (cliente != null) {
                tipoUsuarioLogado = "CLIENTE"

                // AQUI ESTÁ A CHAVE DO PROBLEMA:
                // Passamos o nome que veio do banco para o sharedViewModel
                sharedViewModel.nome = cliente.nome

                irParaHome = true
                loginErro = false
            } else {
                loginErro = true
            }
        }
    }

    fun limparCampos() {
        usuario = ""
        senha = ""
        loginErro = false
        irParaHome = false
        irParaTrocaSenha = false
        tipoUsuarioLogado = ""
        usuarioLogado = null
    }
}