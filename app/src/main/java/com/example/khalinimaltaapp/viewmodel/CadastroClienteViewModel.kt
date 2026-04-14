package com.example.khalinimaltaapp.viewmodel

import com.example.khalinimaltaapp.data.Cliente
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// Mudamos para AndroidViewModel para ele conseguir acessar o Banco de Dados
class CadastroClienteViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Conexão com o banco de dados
    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()

    // 2. Todas as variáveis que a tela precisa
    var nome by mutableStateOf("")
    var sobrenome by mutableStateOf("")
    var dataNasc by mutableStateOf("")
    var cpf by mutableStateOf("")
    var foneCelular by mutableStateOf("")
    var email by mutableStateOf("")
    var complemento by mutableStateOf("")
    var concordoLGPD by mutableStateOf(false)

    // Variáveis de senha (que antes estavam no outro arquivo)
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")

    // 3. Função única para salvar tudo de uma vez
    fun salvarNoBanco() {
        if (senha == confirmarSenha && senha.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val novoCliente = Cliente(
                        nome = nome,
                        sobrenome = sobrenome,
                        data = dataNasc, // Ajustado para o nome que o seu Model Cliente espera
                        cpf = cpf,
                        telefone = foneCelular,
                        email = email,
                        senha = senha
                    )

                    clienteDao.inserir(novoCliente)
                    println("SUCESSO: ${novoCliente.nome} cadastrado com sucesso!")

                    // Limpa a tela após salvar
                    limparCampos()

                } catch (e: Exception) {
                    println("ERRO AO SALVAR: ${e.message}")
                }
            }
        }
    }

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