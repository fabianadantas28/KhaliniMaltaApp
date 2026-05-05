package com.example.khalinimaltaapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.khalinimaltaapp.viewmodel.CadastroProdutoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaProdutosScreen(
    navController: NavController,
    categoriaSelecionada: String,
    viewModel: CadastroProdutoViewModel
) {
    val produtos by viewModel.getProdutosPorCategoria(categoriaSelecionada).collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo_logomarca),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                // Aumentamos a altura da barra para o nome "ANÉIS" descer
                Column {
                    Spacer(modifier = Modifier.height(100.dp)) // ESPAÇO PARA O NOME DESCER
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                categoriaSelecionada.uppercase(),
                                color = CorOuroBorda,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = CorOuroBorda)
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent // Deixamos transparente para não cobrir a marca
                        )
                    )
                }
            }
        ) { paddingValues ->
            if (produtos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum item em estoque", color = Color.White)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // ESPAÇO PARA OS PRODUTOS FICAREM ABAIXO DA LOGOMARCA
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }

                    items(produtos) { produto ->
                        if (produto.qtdeEstoque > 0) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, CorOuroBorda),
                                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f))
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(produto.nomeProduto, color = Color.White, fontWeight = FontWeight.Bold)
                                        Text("R$ ${String.format("%.2f", produto.preco)}", color = CorOuroBorda)
                                        Text("Estoque: ${produto.qtdeEstoque}", color = Color.LightGray, fontSize = 12.sp)
                                    }
                                    // No botão COMPRAR da sua ListaProdutosScreen
                                    Button(
                                        onClick = {
                                            // Navega para a tela de vendas passando o nome do produto e o preço
                                            navController.navigate("vendas?produtoNome=${produto.nomeProduto}&preco=${produto.preco}")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CorOuroBorda)
                                    ) {
                                        Text("COMPRAR", color = Color.Black, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}