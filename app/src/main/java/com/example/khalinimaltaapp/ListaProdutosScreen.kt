package com.example.khalinimaltaapp

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
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
import androidx.navigation.NavController
import com.example.khalinimaltaapp.viewmodel.CadastroProdutoViewModel
import com.example.khalinimaltaapp.data.Produto
import coil.compose.AsyncImage // Importação da biblioteca Coil adicionada

val CorOuroBordaLista = Color(0xFFC79E5E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaProdutosScreen(
    navController: NavController,
    categoriaSelecionada: String,
    viewModel: CadastroProdutoViewModel
) {
    val produtos by viewModel.buscarPorCategoria(categoriaSelecionada).collectAsState(initial = emptyList())

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
                Column {
                    Spacer(modifier = Modifier.height(60.dp))
                    CenterAlignedTopAppBar(
                        title = {
                            Text(categoriaSelecionada.uppercase(), color = CorOuroBordaLista, fontWeight = FontWeight.Bold)
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = CorOuroBordaLista)
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
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
                    modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(produtos) { produto ->
                        if (produto.qtdeEstoque > 0) {
                            ItemProdutoCard(produto, navController)
                        }
                    }
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                }
            }
        }
    }
}

@Composable
fun ItemProdutoCard(produto: Produto, navController: NavController) {
    var quantidadeSelecionada by remember { mutableIntStateOf(1) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, CorOuroBordaLista),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // FOTO OTIMIZADA COM COIL (CORRIGIDO)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, CorOuroBordaLista.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (!produto.imagemUri.isNullOrEmpty()) {
                    AsyncImage(
                        model = produto.imagemUri,
                        contentDescription = "Foto de ${produto.nomeProduto}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        // Caso a permissão expire em URI real, mostra um ícone padrão elegante
                        error = painterResource(id = R.drawable.acessorios),
                        placeholder = painterResource(id = R.drawable.acessorios)
                    )
                } else {
                    Icon(
                        Icons.Default.Image,
                        "Sem foto",
                        tint = CorOuroBordaLista.copy(alpha = 0.3f),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // TEXTOS E PREÇO
            Column(modifier = Modifier.weight(1f)) {
                Text(produto.nomeProduto, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("R$ ${String.format("%.2f", produto.preco)}", color = CorOuroBordaLista)

                // Quantidade
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (quantidadeSelecionada > 1) quantidadeSelecionada-- }, modifier = Modifier.size(24.dp)) {
                        Text("-", color = CorOuroBordaLista, fontSize = 20.sp)
                    }
                    Text(text = quantidadeSelecionada.toString(), color = Color.White, modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = { if (quantidadeSelecionada < produto.qtdeEstoque) quantidadeSelecionada++ }, modifier = Modifier.size(24.dp)) {
                        Text("+", color = CorOuroBordaLista, fontSize = 20.sp)
                    }
                }
            }

            // BOTÃO
            Button(
                onClick = {
                    navController.navigate("vendas?produtoNome=${produto.nomeProduto}&preco=${produto.preco}&quantidade=${quantidadeSelecionada}")
                },
                colors = ButtonDefaults.buttonColors(containerColor = CorOuroBordaLista),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("COMPRAR", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}