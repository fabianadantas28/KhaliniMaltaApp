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

    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()

    // Variáveis de estado
    var nome by mutableStateOf("")
    var sobrenome by mutableStateOf("")
    var dataNasc by mutableStateOf("")
    var cpf by mutableStateOf("")
    var foneCelular by mutableStateOf("")
    var email by mutableStateOf("")
    var complemento by mutableStateOf("")
    var concordoLGPD by mutableStateOf(false)
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")

    // --- NOVA FUNÇÃO PARA CONSERTAR O ERRO NA MAINACTIVITY ---
    fun atualizarNome(novoNome: String) {
        nome = novoNome
    }

    fun salvarNoBanco() {
        if (senha == confirmarSenha && senha.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val novoCliente = Cliente(
                        nome = nome,
                        sobrenome = sobrenome,
                        data = dataNasc,
                        cpf = cpf,
                        telefone = foneCelular,
                        email = email,
                        senha = senha
                    )
                    clienteDao.inserir(novoCliente)
                    Log.d("DB_SUCCESS", "Cliente ${novoCliente.nome} salvo com sucesso!")

                    // Limpa apenas dados de senha após cadastro
                    senha = ""
                    confirmarSenha = ""
                } catch (e: Exception) {
                    Log.e("DB_ERROR", "Erro ao inserir cliente: ${e.message}")
                }
            }
        }
    }

    /**
     * Função chamada no Logout pela MainActivity
     */
    fun limparParaSair() {
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