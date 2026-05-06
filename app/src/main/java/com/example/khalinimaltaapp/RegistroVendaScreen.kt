package com.example.khalinimaltaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.khalinimaltaapp.viewmodel.RegistroVendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroVendaScreen(
    navController: NavController,
    vModel: RegistroVendaViewModel,
    produtoNome: String = "",
    produtoPreco: String = "",
    quantidade: Int = 1 // 1. NOVO: Recebe a quantidade escolhida
) {
    // Conversão do preço para número para podermos calcular
    val precoUnitario = produtoPreco.toDoubleOrNull() ?: 0.0
    val totalVenda = precoUnitario * quantidade

    var nomeClienteTexto by remember { mutableStateOf("Cliente Logado") }
    var expandido by remember { mutableStateOf(false) }
    var formaPagamento by remember { mutableStateOf("Selecione...") }
    val opcoesPagamento = listOf("Pix", "Cartão de Crédito", "Cartão de Débito", "Dinheiro")

    val corOuro = Color(0xFFC39953)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp).verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(110.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = corOuro)
            }
            Text("FINALIZAR COMPRA", color = corOuro, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- DADOS DO ITEM E QUANTIDADE ---
        Text("RESUMO DO PEDIDO", color = corOuro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
            border = androidx.compose.foundation.BorderStroke(1.dp, corOuro.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = produtoNome, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = "Qtd: $quantidade", color = Color.White) // MOSTRA A QUANTIDADE
                }

                Spacer(modifier = Modifier.height(8.dp))

                Divider(color = Color.Gray.copy(alpha = 0.3f))

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Preço Unitário", color = Color.Gray, fontSize = 13.sp)
                    Text(text = "R$ ${String.format("%.2f", precoUnitario)}", color = Color.Gray, fontSize = 13.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "TOTAL", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        text = "R$ ${String.format("%.2f", totalVenda)}",
                        color = corOuro,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- FORMA DE PAGAMENTO (Mantemos igual, mas agora o botão envia os dados certos) ---
        Text("FORMA DE PAGAMENTO", color = corOuro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        // ... (Mantém o código do Dropdown e campos de cartão que você já tem)

        Spacer(modifier = Modifier.height(40.dp))

        // --- BOTÃO FINALIZAR ---
        Button(
            onClick = {
                // AGORA O VIEWMODEL RECEBE TUDO: Nome, Preço Total e Quantidade
                vModel.confirmarVenda(
                    nomeProduto = produtoNome,
                    quantidade = quantidade,
                    valorTotal = totalVenda,
                    formaPagamento = formaPagamento,
                    onSucesso = {
                        navController.navigate("confirmacao_venda") // Exemplo de rota de sucesso
                    }
                )
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            enabled = (formaPagamento != "Selecione..."),
            colors = ButtonDefaults.buttonColors(containerColor = corOuro)
        ) {
            Text("FINALIZAR COMPRA", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}