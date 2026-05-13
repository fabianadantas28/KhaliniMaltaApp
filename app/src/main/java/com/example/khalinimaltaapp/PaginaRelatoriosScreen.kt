package com.example.khalinimaltaapp.ui.relatorio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.viewmodel.RelatoriosViewModel

val KhaliniGold = Color(0xFFC39953)
val CardDarkBlue = Color(0xFF121A24)

@Composable
fun PaginaRelatorio(
    onVoltar: () -> Unit = {},
    viewModel: RelatoriosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Dados trazidos pelo João (Vendas)
    val totalVendas by viewModel.totalVendas.collectAsState(initial = 0)
    val receitaTotal by viewModel.receitaTotal.collectAsState(initial = 0.0)
    val produtoMaisVendido by viewModel.produtoMaisVendido.collectAsState(initial = "---")
    val formaPagamentoMaisUsada by viewModel.formaPagamentoMaisUsada.collectAsState(initial = "---")

    // Seus dados (Estoque e Clientes)
    val totalProdutos by viewModel.totalProdutos.collectAsState(initial = 0)
    val totalClientes by viewModel.totalClientes.collectAsState(initial = 0)
    val valorTotalEstoque by viewModel.valorTotalEstoque.collectAsState(initial = 0.0)
    val totalItensEstoque by viewModel.totalItensEstoque.collectAsState(initial = 0)
    val ticketMedio by viewModel.ticketMedio.collectAsState(initial = 0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState()) // Adicionado scroll para caber tudo
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVoltar) {
                Icon(Icons.Default.ArrowBack, "Voltar", tint = KhaliniGold)
            }
            Text("Relatórios", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.Refresh, "Atualizar", tint = KhaliniGold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SEÇÃO 1: VENDAS (Novidade do João)
        SecaoTitulo("📊 Resumo de Vendas")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CardInfo("Receita Total", "R$ ${"%.2f".format(receitaTotal)}", Modifier.weight(1.1f))
            CardInfo("Total Vendas", "$totalVendas", Modifier.weight(0.9f))
            CardInfo("Ticket Médio", "R$ ${"%.2f".format(ticketMedio)}", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CardInfo("Pgto + Usado", formaPagamentoMaisUsada, Modifier.weight(1f))
            CardInfo("Produto + Vendido", produtoMaisVendido, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SEÇÃO 2: ESTOQUE E CLIENTES
        SecaoTitulo("📦 Resumo de Ativos")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CardInfo("Valor Estoque", "R$ ${"%.2f".format(valorTotalEstoque)}", Modifier.weight(1.1f))
            CardInfo("Produtos", "$totalProdutos", Modifier.weight(0.9f))
            CardInfo("Clientes", "$totalClientes", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SEÇÃO 3: SEU GRÁFICO (O diferencial visual)
        GraficoVendas()

        Spacer(modifier = Modifier.height(24.dp))

        BotoesRelatorio()

        Spacer(modifier = Modifier.height(8.dp))
        Text("● Dados atualizados em tempo real", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}

@Composable
fun SecaoTitulo(texto: String) {
    Text(text = texto, color = KhaliniGold, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
fun CardInfo(titulo: String, valor: String, modifier: Modifier) {
    Card(
        modifier = modifier.height(90.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBlue),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, Color.Gray.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(titulo, color = Color.Gray, fontSize = 10.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(4.dp))
            Text(valor, color = if (valor.contains("R$")) KhaliniGold else Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun GraficoVendas() {
    Card(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBlue),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Evolução de Vendas (Semestre)", color = KhaliniGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                val pesos = listOf(0.3f, 0.5f, 0.4f, 0.7f, 0.9f, 0.6f)
                val meses = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun")
                pesos.forEachIndexed { index, peso ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.width(20.dp).fillMaxHeight(peso).background(if(index == 4) KhaliniGold else KhaliniGold.copy(alpha = 0.3f), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                        Text(meses[index], color = Color.Gray, fontSize = 9.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BotoesRelatorio() {
    OutlinedButton(
        onClick = { /* Exportar PDF */ },
        modifier = Modifier.fillMaxWidth().height(48.dp),
        border = BorderStroke(1.dp, KhaliniGold),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(Icons.Default.Share, null, tint = KhaliniGold)
        Spacer(Modifier.width(8.dp))
        Text("EXPORTAR RELATÓRIO PDF", color = KhaliniGold, fontWeight = FontWeight.Bold)
    }
}