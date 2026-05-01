package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import com.example.khalinimaltaapp.data.Produto
import kotlinx.coroutines.flow.Flow

class ControleEstoqueViewModel(private val produtoDao: ProdutoDao) : ViewModel() {
    // Flow que traz todos os produtos do banco automaticamente
    val todosOsProdutos: Flow<List<Produto>> = produtoDao.getAllProdutos()
}