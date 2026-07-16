package com.example.khalinimaltaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Cliente
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Alterado para ViewModel comum, pois o Firebase não precisa do contexto do app aqui
class CriarSenhaViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

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
                        nome = nome,
                        email = email,
                        sobrenome = sobrenome,
                        data = data,
                        cpf = cpf,
                        telefone = telefone,
                        senha = senha
                    )

                    // Salva na coleção "clientes" utilizando o CPF como ID do documento
                    firestore.collection("clientes")
                        .document(novoCliente.cpf)
                        .set(novoCliente)
                        .await()

                    limparCampos()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun limparCampos() {
        nome = ""; sobrenome = ""; data = ""; cpf = ""; telefone = ""; email = ""; senha = ""; confirmarSenha = ""
    }
}