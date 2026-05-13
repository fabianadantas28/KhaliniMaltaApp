package com.example.khalinimaltaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.khalinimaltaapp.viewmodel.EntradaEstoqueViewModel

@Composable
fun EntradaEstoqueScreen(navController: NavController, vModel: EntradaEstoqueViewModel) {
    val produtos by vModel.produtosEncontrados.collectAsState()
    val corOuro = Color(0xFFC39953)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // --- CABEÇALHO COM SETA DE VOLTAR ---
        Spacer(modifier = Modifier.height(60.dp)) // Espaço para a barra de status

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = corOuro,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "ENTRADA DE ESTOQUE",
                color = corOuro,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        // ------------------------------------

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Busca
        OutlinedTextField(
            value = vModel.buscaTexto,
            onValueChange = { vModel.buscarProduto(it) },
            label = { Text("Buscar Joia para Reposição", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = corOuro,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lógica de exibição: Lista ou Formulário de Edição
        if (vModel.produtoSelecionado == null) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(produtos) { produto ->
                    ListItem(
                        modifier = Modifier.clickable { vModel.produtoSelecionado = produto },
                        headlineContent = { Text(produto.nomeProduto, color = Color.White) },
                        supportingContent = { Text("Estoque Atual: ${produto.qtdeEstoque} un", color = Color.Gray) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                    HorizontalDivider(color = Color.DarkGray)
                }
            }
        } else {
            // Formulário quando um produto é selecionado
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Produto: ${vModel.produtoSelecionado?.nomeProduto}",
                        color = corOuro,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = vModel.quantidadeAdicional,
                        onValueChange = { vModel.quantidadeAdicional = it },
                        label = { Text("Quantidade que Chegou", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = corOuro
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Botão Cancelar
                        OutlinedButton(
                            onClick = { vModel.produtoSelecionado = null },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                        ) {
                            Text("CANCELAR")
                        }

                        // Botão Confirmar
                        Button(
                            onClick = {
                                vModel.confirmarEntrada {
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = corOuro)
                        ) {
                            Text("ATUALIZAR", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}