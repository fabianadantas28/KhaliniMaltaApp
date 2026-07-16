package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Cliente
import com.example.khalinimaltaapp.data.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Convertido para ViewModel comum (Firebase dispensa o contexto de application aqui)
class LoginViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

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

            try {
                // 2. TENTATIVA DE LOGIN: FUNCIONÁRIO/ADMIN CADASTRADO
                // Como salvamos o usuário usando o e-mail como ID do documento, consultamos direto por ele
                val docUsuario = firestore.collection("usuarios")
                    .document(usuarioDigitadoMinusculo)
                    .get()
                    .await()

                if (docUsuario.exists()) {
                    val func = docUsuario.toObject(Usuario::class.java)
                    if (func != null && func.senha == senha) {
                        withContext(Dispatchers.Main) {
                            usuarioLogado = func
                            tipoUsuarioLogado = func.perfil.uppercase().trim()
                            sharedViewModel.nome = func.nome

                            // Verifica se precisa trocar a senha provisória
                            if (func.trocarSenha) {
                                irParaTrocaSenha = true
                            } else {
                                irParaHome = true
                            }
                            loginErro = false
                        }
                        return@launch
                    }
                }

                // 3. TENTATIVA DE LOGIN: CLIENTE (Busca por E-mail ou por CPF)
                // Primeiro tentamos buscar pelo CPF (caso o cliente digite o CPF como login e este seja o ID do documento)
                var docCliente = firestore.collection("clientes")
                    .document(textoDigitadoOriginal)
                    .get()
                    .await()

                var clienteLogado: Cliente? = null

                if (docCliente.exists()) {
                    val cliente = docCliente.toObject(Cliente::class.java)
                    if (cliente != null && cliente.senha == senha) {
                        clienteLogado = cliente
                    }
                }

                // Se não achou pelo CPF, fazemos uma Query na nuvem buscando pelo campo de email
                if (clienteLogado == null) {
                    val queryEmail = firestore.collection("clientes")
                        .whereEqualTo("email", usuarioDigitadoMinusculo)
                        .get()
                        .await()

                    if (!queryEmail.isEmpty) {
                        val cliente = queryEmail.documents.first().toObject(Cliente::class.java)
                        if (cliente != null && cliente.senha == senha) {
                            clienteLogado = cliente
                        }
                    }
                }

                // Resposta final do Login para a interface
                withContext(Dispatchers.Main) {
                    if (clienteLogado != null) {
                        tipoUsuarioLogado = "CLIENTE"
                        sharedViewModel.nome = clienteLogado.nome
                        irParaHome = true
                        loginErro = false
                    } else {
                        loginErro = true
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
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