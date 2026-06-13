package com.example.khalinimaltaapp

import com.example.khalinimaltaapp.viewmodel.ControleEstoqueViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.database.AppDatabase
import coil.compose.AsyncImage // Importação da biblioteca Coil adicionada

// CORES DA KHALINI
val KhaliniGold = Color(0xFFC39953)
val CardBackground = Color(0xFF121A24)
val StatusGreen = Color(0xFF00C853)
val StatusOrange = Color(0xFFF57C00)
val StatusRed = Color(0xFFD50000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaginaControleEstoque(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val viewModel: ControleEstoqueViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ControleEstoqueViewModel(db.produtoDao()) as T
            }
        }
    )

    val produtos by viewModel.produtosFiltrados.collectAsState()
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
                .padding(horizontal = 16.dp)
        ) {
            // --- CABEÇALHO COM SETA DE VOLTAR ---
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = KhaliniGold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Build, null, tint = KhaliniGold, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Controle de Estoque",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = viewModel.buscaTexto,
                onValueChange = { viewModel.buscaTexto = it },
                placeholder = { Text("Buscar produto...", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = KhaliniGold) },
                trailingIcon = {
                    if (viewModel.buscaTexto.isNotEmpty()) {
                        IconButton(onClick = { viewModel.buscaTexto = "" }) {
                            Icon(Icons.Default.Close, null, tint = Color.Gray)
                        }
                    }
                },
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
                    val message = if (viewModel.buscaTexto.isEmpty()) "Nenhum produto cadastrado." else "Nenhum resultado encontrado."
                    Text(message, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(produtos) { produto ->
                        CardProdutoEstoque(produto)
                    }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- NOVO: CONTAINER DA FOTO DO PRODUTO COM COIL ---
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .border(1.dp, KhaliniGold.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (!produto.imagemUri.isNullOrEmpty()) {
                    AsyncImage(
                        model = produto.imagemUri,
                        contentDescription = "Foto de ${produto.nomeProduto}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.acessorios),
                        placeholder = painterResource(id = R.drawable.acessorios)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Sem foto",
                        tint = KhaliniGold.copy(alpha = 0.3f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // --- CONTEÚDO DOS TEXTOS E PREÇOS ---
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                        Text(
                            text = produto.nomeProduto,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "Cód: ${produto.codigoInterno} | ${produto.categoria}",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }

                    // Status da Tag
                    val (txtStatus, corStatus) = when {
                        produto.qtdeEstoque > 10 -> "Em Estoque" to StatusGreen
                        produto.qtdeEstoque > 0 -> "Baixo" to StatusOrange
                        else -> "Esgotado" to StatusRed
                    }

                    Surface(color = corStatus, shape = RoundedCornerShape(6.dp)) {
                        Text(
                            text = txtStatus,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Qtd: ${produto.qtdeEstoque}",
                        color = KhaliniGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "R$ ${String.format("%.2f", produto.preco)}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}