package com.example.khalinimaltaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Venda
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// Removemos o 'vendaDao' do construtor
class GestaoVendasViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

    // 1. Puxa as vendas da nuvem em tempo real ordenadas pela mais recente
    // Nota: Certifique-se de que o campo de data/hora na sua classe Venda corresponda ao nome usado aqui (ex: "dataHora")
    val todasVendas: StateFlow<List<Venda>> = firestore.collection("vendas")
        .orderBy("dataHora", Query.Direction.DESCENDING)
        .snapshots()
        .map { querySnapshot ->
            querySnapshot.toObjects(Venda::class.java)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Calcula o faturamento total somando os valores em tempo real a partir do fluxo de vendas acima
    // Nota: Certifique-se de que a sua classe Venda tenha a propriedade "valorTotal" ou "total" mapeada como Double
    val faturamentoTotal: StateFlow<Double?> = todasVendas
        .map { listaDeVendas ->
            listaDeVendas.sumOf { venda ->
                // Altere 'valorTotal' para o nome exato da propriedade de valor na sua classe Venda
                venda.valorTotal
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )
}