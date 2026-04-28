package com.example.khalinimaltaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// IMPORTANTE: O caminho agora aponta para .data onde você colocou o Produto
import com.example.khalinimaltaapp.data.Produto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaProdutosScreen(
    categoria: String,
    onVoltar: () -> Unit,
    onIrParaCadastro: () -> Unit
) {
    // 1. Criando uma lista de teste com os nomes corretos da sua classe
    val listaDeTeste = listOf(
        Produto(
            nomeProduto = "Produto Exemplo",
            codigoInterno = "001",
            qtdeEstoque = 10,
            preco = 100.0,
            categoria = categoria
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categoria: $categoria", color = Color(0xFFC79E5E)) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFFC79E5E))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onIrParaCadastro,
                containerColor = Color(0xFFC79E5E)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black)
        ) {
            items(listaDeTeste) { item ->
                CartaoItemProduto(item)
            }
        }
    }
}

@Composable
fun CartaoItemProduto(produto: Produto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // AQUI ESTÁ O SEGREDO: Usando os nomes exatos que você definiu
            Text(text = produto.nomeProduto, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Cód: ${produto.codigoInterno}", color = Color.Gray)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "R$ ${produto.preco}", color = Color(0xFFC79E5E), fontWeight = FontWeight.Bold)
                Text(text = "Estoque: ${produto.qtdeEstoque}", color = Color.White)
            }
        }
    }
}