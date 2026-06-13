package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.dao.VendaDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class GestaoVendasViewModel(private val vendaDao: VendaDao) : ViewModel() {

    // Puxa as vendas do banco em ordem correta
    val todasVendas = vendaDao.buscarTodasVendas().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // CORREÇÃO: Puxa a soma direto da Query do SQLite (totalVendas), acabando com o valor sumido
    val faturamentoTotal: StateFlow<Double?> = vendaDao.totalVendas().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )
}