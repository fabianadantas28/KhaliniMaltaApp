package com.example.khalinimaltaapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Removemos o 'produtoDao' do construtor
class CadastroProdutoViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

    // --- 1. ESTADOS PARA O FORMULÁRIO ---
    var nome by mutableStateOf("")
    var marca by mutableStateOf("")
    var material by mutableStateOf("")
    var codigo by mutableStateOf("")
    var preco by mutableStateOf("")
    var descricao by mutableStateOf("")
    var categoria by mutableStateOf("")
    var estoque by mutableStateOf("")

    // Estado para armazenar a URI da imagem selecionada
    var imagemUriState by mutableStateOf<String?>(null)

    // --- 2. FUNÇÃO PARA SALVAR NOVO PRODUTO ---
    fun salvarProduto(onSucesso: () -> Unit, onError: (String) -> Unit) {
        if (nome.isBlank() || categoria.isBlank() || preco.isBlank()) {
            onError("Preencha Nome, Categoria e Preço!")
            return
        }

        viewModelScope.launch {
            try {
                val novoProduto = Produto(
                    nomeProduto = nome,
                    marca = marca,
                    material = material,
                    codigoInterno = codigo,
                    descricao = descricao,
                    categoria = categoria,
                    qtdeEstoque = estoque.toIntOrNull() ?: 0,
                    preco = preco.replace(",", ".").toDoubleOrNull() ?: 0.0,
                    imagemUri = imagemUriState // AGORA SALVA A URI REAL
                )

                // Define o ID do documento: se houver código interno, usa ele. Se não, deixa o Firestore gerar um automático
                val idDocumento = if (novoProduto.codigoInterno.isNotBlank()) novoProduto.codigoInterno else null

                if (idDocumento != null) {
                    firestore.collection("produtos")
                        .document(idDocumento)
                        .set(novoProduto)
                        .await()
                } else {
                    firestore.collection("produtos")
                        .add(novoProduto)
                        .await()
                }

                limparCampos()
                onSucesso()
            } catch (e: Exception) {
                onError("Erro ao salvar: ${e.message}")
            }
        }
    }

    // --- 3. FUNÇÃO PARA A VITRINE ---
    // Retorna um fluxo em tempo real (Flow) dos produtos da nuvem filtrados por categoria
    fun buscarPorCategoria(categoriaNome: String): Flow<List<Produto>> {
        return firestore.collection("produtos")
            .whereEqualTo("categoria", categoriaNome)
            .snapshots() // Escuta as atualizações em tempo real da nuvem
            .map { querySnapshot ->
                querySnapshot.toObjects(Produto::class.java)
            }
    }

    // --- 4. FUNÇÃO DE VENDA ---
    fun venderProduto(produto: Produto) {
        if (produto.qtdeEstoque > 0) {
            viewModelScope.launch {
                try {
                    val novaQuantidade = produto.qtdeEstoque - 1

                    // Se o produto tiver código de barras/interno, atualiza por ele
                    if (produto.codigoInterno.isNotBlank()) {
                        firestore.collection("produtos")
                            .document(produto.codigoInterno)
                            .update("qtdeEstoque", novaQuantidade)
                            .await()
                    }
                } catch (e: Exception) {
                    Log.e("FIREBASE_ERROR", "Erro ao atualizar estoque: ${e.message}")
                }
            }
        }
    }

    // Limpa os campos após o cadastro com sucesso
    private fun limparCampos() {
        nome = ""; marca = ""; material = ""; codigo = ""
        preco = ""; descricao = ""; categoria = ""; estoque = ""
        imagemUriState = null // Limpa a foto também
    }
}