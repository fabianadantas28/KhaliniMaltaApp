package com.example.khalinimaltaapp.viewmodel

import android.util.Log
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

// Removemos a necessidade de passar o ClienteDao no construtor
class CadastroClienteViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

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
                    // Mapeia os dados da tela para o objeto Cliente
                    val novoCliente = Cliente(
                        nome = nome,
                        sobrenome = sobrenome,
                        data = dataNasc,
                        cpf = cpf,
                        telefone = foneCelular,
                        email = email,
                        senha = senha
                    )

                    // Salva no Firestore dentro de uma coleção chamada "clientes"
                    // O "document(novoCliente.cpf)" usa o CPF do cliente como o ID único do documento
                    firestore.collection("clientes")
                        .document(novoCliente.cpf)
                        .set(novoCliente)
                        .await() // Aguarda a tarefa do Firebase terminar de forma assíncrona

                    Log.d("FIREBASE_SUCCESS", "Cliente ${novoCliente.nome} salvo na nuvem com sucesso!")

                } catch (e: Exception) {
                    Log.e("FIREBASE_ERROR", "Erro ao salvar no Firestore: ${e.message}")
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