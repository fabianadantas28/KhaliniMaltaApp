package com.example.khalinimaltaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState // IMPORTANTE
import androidx.compose.runtime.getValue       // IMPORTANTE
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.viewmodel.GestaoVendasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestaoVendasScreen(
    onVoltar: () -> Unit,
    viewModel: GestaoVendasViewModel
) {
    // Coletando os estados com 'by' requer os imports de runtime.getValue
    val vendas by viewModel.todasVendas.collectAsState()
    val totalFaturado by viewModel.faturamentoTotal.collectAsState()
    val corOuro = Color(0xFFC39953)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("GESTÃO DE VENDAS", color = corOuro, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = corOuro)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- CARDS DE RESUMO ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Icon(Icons.Default.AttachMoney, contentDescription = "Faturamento", tint = corOuro)
                        Text("Total Bruto", color = Color.Gray, fontSize = 12.sp)
                        // Garante que o valor não seja nulo ao formatar
                        Text("R$ ${String.format("%.2f", totalFaturado ?: 0.0)}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Vendas", tint = corOuro)
                        Text("Vendas", color = Color.Gray, fontSize = 12.sp)
                        Text("${vendas.size}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("HISTÓRICO DETALHADO", color = corOuro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // --- LISTA DE VENDAS ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vendas) { venda ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(venda.nomeCliente.uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                                // Sincronizado com o campo dataHora da sua classe Venda.kt
                                Text(venda.dataHora, color = Color.Gray, fontSize = 12.sp)
                                Text("Pagamento: ${venda.formaPagamento}", color = corOuro, fontSize = 11.sp)
                                Text("Produto: ${venda.nomeProduto}", color = Color.LightGray, fontSize = 11.sp)
                            }
                            Text(
                                "R$ ${String.format("%.2f", venda.valorTotal)}",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}