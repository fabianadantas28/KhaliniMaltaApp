package com.example.khalinimaltaapp.ui.estoque

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 1. MODELO DE DADOS ---
data class ProdutoEstoque(
    val nome: String,
    val codigo: String,
    val categoria: String,
    val quantidade: Int
)

// --- 2. CORES DA KHALINI ---
val KhaliniGold = Color(0xFFC39953)
val CardBackground = Color(0xFF121A24)
val StatusGreen = Color(0xFF00C853)
val StatusOrange = Color(0xFFF57C00)
val StatusRed = Color(0xFFD50000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaginaControleEstoque() {
    val produtos = listOf(
        ProdutoEstoque("Brinco Prateado", "BPZ001", "Brincos Prateados", 45),
        ProdutoEstoque("Anel de Prata 925", "APA001", "Anéis Prateados", 12),
        ProdutoEstoque("Pulseira Prateada Feminina", "PPF001", "Pulseiras Prateadas", 0),
        ProdutoEstoque("Corrente Prateada Masculina", "CPM001", "Correntes Prateadas", 89)
    )

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D1117))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total de itens: 176", color = KhaliniGold, fontSize = 12.sp)
                Text("04/10/2025, 17:51", color = Color.Gray, fontSize = 10.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // --- CABEÇALHO (Ícones Universais) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Mudei para 'Build' que é um ícone que SEMPRE funciona
                    Icon(Icons.Default.Build, null, tint = KhaliniGold, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Controle de\nEstoque",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.ShoppingCart, null, tint = KhaliniGold, modifier = Modifier.size(20.dp))
                    Icon(Icons.Default.Info, null, tint = KhaliniGold, modifier = Modifier.size(20.dp))
                    Icon(Icons.Default.Refresh, null, tint = KhaliniGold, modifier = Modifier.size(20.dp))
                    Icon(Icons.Default.Settings, null, tint = KhaliniGold, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- BARRA DE BUSCA ---
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar...", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = KhaliniGold) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = CardBackground,
                    focusedContainerColor = CardBackground,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = KhaliniGold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- BOTÕES (Ícones Universais) ---
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = KhaliniGold),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.Black)
                    Text(" Entrada", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = KhaliniGold),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    // Ícone de remover/saída
                    Icon(Icons.Default.Clear, null, tint = Color.Black)
                    Text(" Saída", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- LISTA ---
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(produtos) { produto ->
                    CardProdutoEstoque(produto)
                }
            }
        }
    }
}

@Composable
fun CardProdutoEstoque(produto: ProdutoEstoque) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(produto.nome, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Código: ${produto.codigo}", color = Color.Gray, fontSize = 13.sp)
                }

                val (txtStatus, corStatus) = when {
                    produto.quantidade > 20 -> "Em Estoque" to StatusGreen
                    produto.quantidade > 0 -> "Estoque Baixo" to StatusOrange
                    else -> "Sem Estoque" to StatusRed
                }

                Surface(color = corStatus, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        txtStatus,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Quantidade: ${produto.quantidade}", color = KhaliniGold, fontWeight = FontWeight.Bold)
                Text("Ver detalhes", color = KhaliniGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEstoqueFinal() {
    PaginaControleEstoque()
}