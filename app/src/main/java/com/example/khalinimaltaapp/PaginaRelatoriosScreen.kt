package com.example.khalinimaltaapp.ui.relatorio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    // Coleta os estados do ViewModel
    val totalProdutos by viewModel.totalProdutos.collectAsState()
    val totalClientes by viewModel.totalClientes.collectAsState()
    val valorTotalEstoque by viewModel.valorTotalEstoque.collectAsState()
    val totalItensEstoque by viewModel.totalItensEstoque.collectAsState()
    val ticketMedio by viewModel.ticketMedio.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Header com botão voltar funcional
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVoltar) {
                Icon(Icons.Default.ArrowBack, "Voltar", tint = KhaliniGold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, null, tint = KhaliniGold, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("Relatórios", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Default.Refresh, "Atualizar", tint = KhaliniGold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "▽ Resumo do Estoque",
            color = KhaliniGold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Cards com dados reais
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CardInfo(
                titulo = "Valor do Estoque",
                valor = "R$ ${"%.2f".format(valorTotalEstoque)}",
                modifier = Modifier.weight(1.1f)
            )
            CardInfo(
                titulo = "Itens em Estoque",
                valor = "$totalItensEstoque",
                modifier = Modifier.weight(0.9f)
            )
            CardInfo(
                titulo = "Ticket Médio",
                valor = "R$ ${"%.2f".format(ticketMedio)}",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cards de clientes e produtos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CardInfo(
                titulo = "Clientes Cadastrados",
                valor = "$totalClientes",
                modifier = Modifier.weight(1f)
            )
            CardInfo(
                titulo = "Produtos Cadastrados",
                valor = "$totalProdutos",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        GraficoVendas()

        Spacer(modifier = Modifier.weight(1f))

        BotoesRelatorio()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("● Dados em tempo real", color = Color.Gray, fontSize = 10.sp)
            Text("Room Database", color = Color.Gray, fontSize = 10.sp)
        }
    }
}

@Composable
fun CardInfo(titulo: String, valor: String, modifier: Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBlue),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(titulo, color = Color.Gray, fontSize = 11.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                valor,
                color = if (valor.contains("R$")) KhaliniGold else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun GraficoVendas() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBlue),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Evolução de Vendas", color = KhaliniGold, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                val alturas = listOf(0.5f, 0.5f, 0.3f, 0.6f, 0.55f, 0.7f)
                val meses = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun")
                alturas.forEachIndexed { index, altura ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .fillMaxHeight(altura)
                                .background(KhaliniGold, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(meses[index], color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BotoesRelatorio() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            border = BorderStroke(1.dp, KhaliniGold),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Build, null, tint = KhaliniGold)
            Spacer(Modifier.width(8.dp))
            Text("Gerar Relatório", color = KhaliniGold)
        }

        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            border = BorderStroke(1.dp, KhaliniGold),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Share, null, tint = KhaliniGold)
            Spacer(Modifier.width(8.dp))
            Text("Exportar PDF", color = KhaliniGold)
        }
    }
}
