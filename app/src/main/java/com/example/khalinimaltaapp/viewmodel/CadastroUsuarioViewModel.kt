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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CadastroUsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()

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

                val novoUsuario = Usuario(
                    nome = nome.trim(),
                    email = email.trim().lowercase(),
                    senha = senhaTemporaria,
                    datahora = agora,
                    perfil = perfil,
                    trocarSenha = true // Garante a regra de negócio da Fabiana: troca obrigatória
                )

                usuarioDao.cadastrarUsuario(novoUsuario)

                // Switch para a Main Thread para atualizar interface
                launch(Dispatchers.Main) {
                    salvouComSucesso = true
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    mensagemErro = "Erro ao salvar: ${e.message}"
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
