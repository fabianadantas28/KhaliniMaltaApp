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

    // Controle de navegação após login
    var irParaHome by mutableStateOf(false)
    var irParaTrocaSenha by mutableStateOf(false)

    // Guarda o usuário logado para usar na TrocaSenhaScreen
    var usuarioLogado: Usuario? = null

    fun fazerLogin() {
        if (usuario.isBlank() || senha.isBlank()) {
            loginErro = true
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            // --- BLOCO DE EMERGÊNCIA PARA PRIMEIRO ACESSO ---
            // Se você digitar admin / admin e o banco estiver vazio, ele te deixa entrar
            if (usuario.trim().lowercase() == "admin" && senha == "admin") {
                val adminNoBanco = usuarioDao.realizarLogin("admin", "admin")

                if (adminNoBanco == null) {
                    // Se não existir, criamos o administrador agora mesmo
                    val novoAdmin = Usuario(
                        nome = "Administrador Master",
                        email = "admin",
                        senha = "admin",
                        perfil = "ADMIN",
                        trocarSenha = false // O admin mestre não precisa trocar
                    )
                    usuarioDao.cadastrarUsuario(novoAdmin)
                    usuarioLogado = novoAdmin
                } else {
                    usuarioLogado = adminNoBanco
                }

                irParaHome = true
                loginErro = false
                return@launch
            }
            // --- FIM DO BLOCO DE EMERGÊNCIA ---

            // Tenta login como funcionário/admin cadastrado
            val func = usuarioDao.realizarLogin(usuario.trim().lowercase(), senha)
            if (func != null) {
                usuarioLogado = func
                if (func.trocarSenha) {
                    irParaTrocaSenha = true  // primeiro acesso → obriga troca de senha
                } else {
                    irParaHome = true
                }
                loginErro = false
                return@launch
            }

            // Tenta login como cliente
            val cliente = clienteDao.buscarPorEmailESenha(usuario.trim().lowercase(), senha)
            if (cliente != null) {
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
        usuarioLogado = null
    }
}