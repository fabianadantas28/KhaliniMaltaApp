package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import com.example.khalinimaltaapp.data.dao.VendaDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegistroVendaViewModel(
    private val produtoDao: ProdutoDao,
    private val vendaDao: VendaDao
) : ViewModel() {

    private val _vendaConfirmada = MutableStateFlow(false)
    val vendaConfirmada = _vendaConfirmada.asStateFlow()

    private val _erroVenda = MutableStateFlow("")
    val erroVenda = _erroVenda.asStateFlow()

    // AJUSTE: Função que a RegistroVendaScreen vai chamar
    fun confirmarVenda(
        nomeProduto: String,
        quantidade: Int,
        valorTotal: Double,
        formaPagamento: String,
        onSucesso: () -> Unit // Callback para avisar a tela
    ) {
        viewModelScope.launch {
            try {
                // 1. Primeiro, buscamos o produto real no banco pelo nome
                // Usamos 'first()' para pegar a lista atual do Flow e extrair o primeiro item
                val listaProdutos = produtoDao.buscarProdutosPorNome(nomeProduto).first()
                val produtoOriginal = listaProdutos.firstOrNull()

                if (produtoOriginal == null) {
                    _erroVenda.value = "Produto não encontrado!"
                    return@launch
                }

                // 2. Validamos o estoque
                if (produtoOriginal.qtdeEstoque < quantidade) {
                    _erroVenda.value = "Estoque insuficiente! Temos apenas ${produtoOriginal.qtdeEstoque}."
                    return@launch
                }

                // 3. Atualizamos o estoque no banco usando a função que criamos ontem
                val novaQuantidade = produtoOriginal.qtdeEstoque - quantidade
                produtoDao.atualizarEstoque(produtoOriginal.id, novaQuantidade)

                // 4. (Opcional) Aqui você poderia salvar na tabela de Vendas se quiser
                // vendaDao.inserir(Venda(...))

                // 5. Sucesso!
                _vendaConfirmada.value = true
                _erroVenda.value = ""
                onSucesso() // Executa a navegação de volta ou para tela de sucesso

            } catch (e: Exception) {
                _erroVenda.value = "Erro ao processar venda: ${e.message}"
            }
        }
    }
}