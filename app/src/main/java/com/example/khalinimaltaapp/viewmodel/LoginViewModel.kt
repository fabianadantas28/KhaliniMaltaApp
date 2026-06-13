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
import kotlinx.coroutines.withContext

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

    fun fazerLogin(sharedViewModel: CadastroClienteViewModel) {
        if (usuario.isBlank() || senha.isBlank()) {
            loginErro = true
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val textoDigitadoOriginal = usuario.trim()
            val usuarioDigitadoMinusculo = textoDigitadoOriginal.lowercase()

            // 1. LOGIN ADMIN de emergência
            if (usuarioDigitadoMinusculo == "admin" && senha == "admin") {
                withContext(Dispatchers.Main) {
                    sharedViewModel.nome = "Administrador"
                    tipoUsuarioLogado = "ADMIN"
                    irParaHome = true
                    loginErro = false
                }
                return@launch
            }


            // 2. LOGIN FUNCIONÁRIO/ADMIN CADASTRADO
            val func = usuarioDao.realizarLogin(usuarioDigitadoMinusculo, senha)
                ?: usuarioDao.realizarLogin(textoDigitadoOriginal, senha)

            if (func != null) {
                withContext(Dispatchers.Main) {
                    usuarioLogado = func
                    tipoUsuarioLogado = func.perfil.uppercase().trim()
                    sharedViewModel.nome = func.nome

                    // Verifica se precisa trocar a senha provisória
                    if (func.trocarSenha) {
                        irParaTrocaSenha = true // Disparará a navegação para a troca de senha
                    } else {
                        irParaHome = true // Vai direto para o painel
                    }
                    loginErro = false
                }
                return@launch
            }


            // 3. LOGIN CLIENTE (Busca por E-mail minúsculo, E-mail original ou por CPF)
            var cliente = clienteDao.buscarPorEmailESenha(usuarioDigitadoMinusculo, senha)

            if (cliente == null) {
                if (cliente == null) {
                    // CORREÇÃO AQUI: Mudado de 'senate' para 'senha'
                    cliente = clienteDao.buscarPorEmailESenha(textoDigitadoOriginal, senha)
                }
            }

            // Tentativa extra: Se o seu ClienteDao tiver busca por CPF, você pode descomentar a lógica abaixo no futuro:
            // if (cliente == null) {
            //     cliente = clienteDao.buscarPorCpfESenha(textoDigitadoOriginal, senha)
            // }

            withContext(Dispatchers.Main) {
                if (cliente != null) {
                    tipoUsuarioLogado = "CLIENTE"
                    sharedViewModel.nome = cliente.nome
                    irParaHome = true
                    loginErro = false
                } else {
                    loginErro = true
                }
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