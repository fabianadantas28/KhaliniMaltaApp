package com.example.khalinimaltaapp.model

import java.util.UUID // Importação essencial para gerar o ID único

// Este modelo guarda os dados que aparecem no Resumo do Dia
data class Dashboard(
    // Gera um identificador único para cada resumo criado
    val id: String = UUID.randomUUID().toString(),
    val vendasMes: Double = 0.0,      // Valor total que aparece no ícone "$"
    val quantidadeAlertas: Int = 0,   // Número de avisos no ícone de "check"
    val novosClientes: Int = 0,       // Contagem de clientes novos no ícone de "lista"
    val vendasDiarias: List<Float> = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f) // As 7 barras do gráfico
)