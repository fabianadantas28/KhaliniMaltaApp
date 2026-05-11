package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.dao.VendaDao // AJUSTE: Verifique se este caminho está correto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GestaoVendasViewModel(private val vendaDao: VendaDao) : ViewModel() {

    // Puxa as vendas do banco usando a função que corrigimos no DAO
    val todasVendas = vendaDao.buscarTodasVendas().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Cálculo do faturamento total (Double? para evitar erro de nulo)
    val faturamentoTotal = todasVendas.map { lista ->
        lista.sumOf { it.valorTotal }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}