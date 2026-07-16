package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Alterado de AndroidViewModel(application) para ViewModel comum
class CadastroUsuarioViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senhaTemporaria by mutableStateOf("")
    var perfil by mutableStateOf("FUNCIONARIO")
    var mensagemErro by mutableStateOf("")
    var salvouComSucesso by mutableStateOf(false)

    fun salvar() {
        // Validações básicas de segurança e preenchimento
        if (nome.isBlank() || email.isBlank() || senhaTemporaria.isBlank()) {
            mensagemErro = "Preencha todos os campos obrigatórios."
            return
        }
        if (!email.contains("@")) {
            mensagemErro = "Informe um e-mail válido."
            return
        }
        if (senhaTemporaria.length < 4) {
            mensagemErro = "A senha temporária deve ter ao menos 4 caracteres."
            return
        }

        mensagemErro = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Versão compatível com API 24 (Android 7.0) para data e hora
                val formatador = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val agora = formatador.format(Date())

                val emailFormatado = email.trim().lowercase()

                val novoUsuario = Usuario(
                    nome = nome.trim(),
                    email = emailFormatado,
                    senha = senhaTemporaria,
                    datahora = agora,
                    perfil = perfil,
                    trocarSenha = true // Garante a regra de negócio: troca obrigatória
                )

                // Salva na coleção "usuarios" usando o email como o ID do documento
                firestore.collection("usuarios")
                    .document(emailFormatado)
                    .set(novoUsuario)
                    .await()

                // Atualiza a interface na Main Thread
                launch(Dispatchers.Main) {
                    salvouComSucesso = true
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    mensagemErro = "Erro ao salvar na nuvem: ${e.message}"
                }
            }
        }
    }

    fun limparCampos() {
        nome = ""
        email = ""
        senhaTemporaria = ""
        perfil = "FUNCIONARIO"
        mensagemErro = ""
        salvouComSucesso = false
    }
}
