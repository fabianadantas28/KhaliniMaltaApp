package com.example.khalinimaltaapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Cliente
import com.example.khalinimaltaapp.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CadastroClienteViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Conexão com o banco de dados via DAO
    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()

    // 2. Variáveis da Tela 1 (Dados Pessoais)
    var nome by mutableStateOf("")
    var sobrenome by mutableStateOf("")
    var dataNasc by mutableStateOf("")
    var cpf by mutableStateOf("")
    var foneCelular by mutableStateOf("")
    var email by mutableStateOf("")
    var complemento by mutableStateOf("")
    var concordoLGPD by mutableStateOf(false)

    // 3. Variáveis da Tela 2 (Senha)
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")

    /**
     * Função que une os dados das duas telas e salva no banco de dados.
     * Ela é chamada pelo botão "Finalizar" na tela de senha.
     */
    fun salvarNoBanco() {
        // Validação básica: as senhas precisam ser iguais e não vazias
        if (senha == confirmarSenha && senha.isNotEmpty()) {

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    // Monta o objeto Cliente com os estados atuais do ViewModel
                    val novoCliente = Cliente(
                        // O 'id' não é passado aqui pois é auto-incremento (id = 0 no model)
                        nome = nome,
                        sobrenome = sobrenome,
                        data = dataNasc,
                        cpf = cpf,
                        telefone = foneCelular,
                        email = email,
                        senha = senha
                    )

                    // Comando que grava efetivamente no SQLite através do Room
                    clienteDao.inserir(novoCliente)

                    Log.d("DB_SUCCESS", "Cliente ${novoCliente.nome} salvo com sucesso!")

                    // Limpa os campos após o sucesso para um novo cadastro futuro
                    limparCampos()

                } catch (e: Exception) {
                    Log.e("DB_ERROR", "Erro ao inserir cliente: ${e.message}")
                }
            }
        } else {
            Log.w("VALIDATION", "Senhas não conferem ou campos obrigatórios vazios.")
        }
    }

    // Função para resetar os campos após o cadastro ou cancelamento
    fun limparCampos() {
        nome = ""
        sobrenome = ""
        dataNasc = ""
        cpf = ""
        foneCelular = ""
        email = ""
        complemento = ""
        concordoLGPD = false
        senha = ""
        confirmarSenha = ""
    }
}