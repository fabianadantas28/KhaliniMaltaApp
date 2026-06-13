package com.example.khalinimaltaapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Cliente
import com.example.khalinimaltaapp.data.dao.ClienteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CadastroClienteViewModel(private val clienteDao: ClienteDao) : ViewModel() {

    // Variáveis estáveis usando o 'by' correto do Kotlin
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

    fun salvarNoBanco() {
        if (senha == confirmarSenha && senha.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    // Mapeia os dados da tela para as colunas exatas da entidade Cliente
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
                    Log.d("DB_SUCCESS", "Cliente ${novoCliente.nome} cadastrado com sucesso!")

                } catch (e: Exception) {
                    Log.e("DB_ERROR", "Erro ao inserir no banco: ${e.message}")
                }
            }
        }
    }

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