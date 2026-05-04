package com.example.khalinimaltaapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.Venda
import com.example.khalinimaltaapp.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RegistroVendaViewModel(application: Application) : AndroidViewModel(application) {

    private val produtoDao = AppDatabase.getDatabase(application).produtoDao()
    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()
    private val vendaDao = AppDatabase.getDatabase(application).vendaDao()

    // Busca de produtos
    private val _produtosEncontrados = MutableStateFlow<List<Produto>>(emptyList())
    val produtosEncontrados = _produtosEncontrados.asStateFlow()

    // Busca de clientes
    private val _clientesEncontrados = MutableStateFlow<List<com.example.khalinimaltaapp.data.Cliente>>(emptyList())
    val clientesEncontrados = _clientesEncontrados.asStateFlow()

    // Estado de feedback para a tela
    val vendaConfirmada = MutableStateFlow(false)
    val erroVenda = MutableStateFlow("")

    fun buscarProduto(query: String) {
        viewModelScope.launch {
            if (query.length > 2) {
                produtoDao.getAllProdutos().collect { lista ->
                    _produtosEncontrados.value = lista.filter {
                        it.nomeProduto.contains(query, ignoreCase = true) ||
                                it.codigoInterno.contains(query)
                    }
                }
            } else {
                _produtosEncontrados.value = emptyList()
            }
        }
    }

    fun buscarCliente(query: String) {
        viewModelScope.launch {
            if (query.length >= 2) {
                clienteDao.getAllClientes().collect { lista ->
                    _clientesEncontrados.value = lista.filter {
                        it.nome.contains(query, ignoreCase = true) ||
                                it.telefone.contains(query)
                    }
                }
            } else {
                _clientesEncontrados.value = emptyList()
            }
        }
    }

    fun confirmarVenda(
        produto: Produto,
        quantidade: Int,
        nomeCliente: String,
        telefoneCliente: String,
        formaPagamento: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Valida estoque
                if (produto.qtdeEstoque < quantidade) {
                    erroVenda.value = "Estoque insuficiente."
                    return@launch
                }

                // 1. Desconta o estoque
                produtoDao.updateProduto(produto.copy(qtdeEstoque = produto.qtdeEstoque - quantidade))

                // 2. Registra a venda no banco
                val agora = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

                vendaDao.registrarVenda(
                    Venda(
                        produtoId = produto.id,
                        nomeProduto = produto.nomeProduto,
                        nomeCliente = nomeCliente.ifBlank { "Cliente não identificado" },
                        telefoneCliente = telefoneCliente,
                        formaPagamento = formaPagamento,
                        quantidade = quantidade,
                        valorTotal = produto.preco * quantidade,
                        dataHora = agora
                    )
                )

                vendaConfirmada.value = true

            } catch (e: Exception) {
                erroVenda.value = "Erro ao registrar venda: ${e.message}"
            }
        }
    }
}
