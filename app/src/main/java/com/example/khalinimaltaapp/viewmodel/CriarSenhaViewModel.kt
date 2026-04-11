package com.example.khalinimaltaapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Cliente
import com.example.khalinimaltaapp.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CriarSenhaViewModel(application: Application) : AndroidViewModel(application) {
    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()

    var nome by mutableStateOf("")
    var sobrenome by mutableStateOf("")
    var data by mutableStateOf("")
    var cpf by mutableStateOf("")
    var telefone by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")

    fun salvarNoBanco() {
        if (senha == confirmarSenha && senha.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val novoCliente = Cliente(
                        nome = nome, email = email, sobrenome = sobrenome,
                        data = data, cpf = cpf, telefone = telefone, senha = senha
                    )
                    clienteDao.inserir(novoCliente)
                    limparCampos()
                } catch (e: Exception) { e.printStackTrace() }
            }
        }
    }

    private fun limparCampos() {
        nome = ""; sobrenome = ""; data = ""; cpf = ""; telefone = ""; email = ""; senha = ""; confirmarSenha = ""
    }
}