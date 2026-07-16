package com.example.khalinimaltaapp.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Removemos o 'produtoDao' do construtor
class EntradaEstoqueViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

    var buscaTexto by mutableStateOf("")
    var quantidadeAdicional by mutableStateOf("")
    var produtoSelecionado by mutableStateOf<Produto?>(null)

    private val _produtosEncontrados = MutableStateFlow<List<Produto>>(emptyList())
    val produtosEncontrados = _produtosEncontrados.asStateFlow()

    fun buscarProduto(nome: String) {
        buscaTexto = nome
        if (nome.isBlank()) {
            _produtosEncontrados.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                // Truque do Firestore para simular o operador "começa com" (Prefix Query)
                firestore.collection("produtos")
                    .whereGreaterThanOrEqualTo("nomeProduto", nome)
                    .whereLessThanOrEqualTo("nomeProduto", nome + "\uf8ff")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val lista = querySnapshot.toObjects(Produto::class.java)
                        _produtosEncontrados.value = lista
                    }
                    .addOnFailureListener { e ->
                        Log.e("FIREBASE_ERROR", "Erro ao buscar produtos: ${e.message}")
                    }
            } catch (e: Exception) {
                Log.e("FIREBASE_ERROR", "Erro na busca: ${e.message}")
            }
        }
    }

    fun confirmarEntrada(onSucesso: () -> Unit) {
        val produto = produtoSelecionado ?: return
        val adicional = quantidadeAdicional.toIntOrNull() ?: 0

        if (adicional > 0) {
            viewModelScope.launch {
                try {
                    val novaQtde = produto.qtdeEstoque + adicional

                    // Se o produto tiver código interno cadastrado, usamos ele como ID do documento
                    if (produto.codigoInterno.isNotBlank()) {
                        firestore.collection("produtos")
                            .document(produto.codigoInterno)
                            .update("qtdeEstoque", novaQtde)
                            .await()

                        onSucesso()
                    } else {
                        // Caso o ID seja o auto-gerado do Firestore e esteja mapeado no seu modelo como 'id'
                        // Certifique-se de que a sua classe de dados Produto tenha um campo para o ID caso precise buscar documentos sem código interno fixo.
                        Log.w("FIREBASE_WARN", "Produto sem códigoInterno definido para atualização.")
                    }
                } catch (e: Exception) {
                    Log.e("FIREBASE_ERROR", "Erro ao atualizar estoque na nuvem: ${e.message}")
                }
            }
        }
    }
}