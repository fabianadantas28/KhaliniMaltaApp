package com.example.khalinimaltaapp.ui.estoque

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.database.AppDatabase
import com.example.khalinimaltaapp.viewmodel.ControleEstoqueViewModel

// CORES DA KHALINI
val KhaliniGold = Color(0xFFC39953)
val CardBackground = Color(0xFF121A24)
val StatusGreen = Color(0xFF00C853)
val StatusOrange = Color(0xFFF57C00)
val StatusRed = Color(0xFFD50000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaginaControleEstoque() {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    // SOLUÇÃO: Criando o ViewModel usando o Factory nativo sem precisar de arquivos extras
    val viewModel: ControleEstoqueViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ControleEstoqueViewModel(db.produtoDao()) as T
            }
        }
    )

    val produtos by viewModel.todosOsProdutos.collectAsState(initial = emptyList())
    val totalItens = produtos.sumOf { it.qtdeEstoque }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D1117))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total de itens em estoque: $totalItens", color = KhaliniGold, fontSize = 12.sp)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar produto...", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = KhaliniGold) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = CardBackground,
                    focusedContainerColor = CardBackground,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = KhaliniGold,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (produtos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum produto cadastrado.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(produtos) { produto ->
                        CardProdutoEstoque(produto)
                    }
                }
            }
        }
    }
}

@Composable
fun CardProdutoEstoque(produto: Produto) {
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
                    Text(produto.nomeProduto, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Cód: ${produto.codigoInterno} | ${produto.categoria}", color = Color.Gray, fontSize = 13.sp)
                }

                val (txtStatus, corStatus) = when {
                    produto.qtdeEstoque > 10 -> "Em Estoque" to StatusGreen
                    produto.qtdeEstoque > 0 -> "Baixo" to StatusOrange
                    else -> "Esgotado" to StatusRed
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
                Text("Qtd: ${produto.qtdeEstoque}", color = KhaliniGold, fontWeight = FontWeight.Bold)
                Text("R$ ${produto.preco}", color = Color.White, fontSize = 14.sp)
            }
        }
    }
}