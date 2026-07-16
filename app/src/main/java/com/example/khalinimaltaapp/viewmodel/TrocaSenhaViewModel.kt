package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Convertido para ViewModel comum (Firebase dispensa o contexto de application aqui)
class TrocaSenhaViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    var novaSenha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")
    var mensagemErro by mutableStateOf("")
    var trocouComSucesso by mutableStateOf(false)

    // Alterado de usuarioId: Int para email: String para bater com a estrutura do Firestore
    fun trocarSenha(email: String) {
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
                // Atualiza a senha e desmarca a flag de troca obrigatória na nuvem
                firestore.collection("usuarios")
                    .document(email.lowercase().trim())
                    .update(
                        mapOf(
                            "senha" to novaSenha,
                            "trocarSenha" to false // Libera o usuário para ir para a Home nos próximos logins
                        )
                    )
                    .await()

                trocouComSucesso = true
            } catch (e: Exception) {
                mensagemErro = "Erro ao atualizar senha na nuvem: ${e.message}"
            }
        }
    }
}