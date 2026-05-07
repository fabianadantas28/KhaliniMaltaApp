package com.example.khalinimaltaapp

import androidx.compose.foundation.background
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
import com.example.khalinimaltaapp.viewmodel.RegistroVendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroVendaScreen(
    navController: NavController,
    vModel: RegistroVendaViewModel
) {
    val navBackStackEntry = navController.currentBackStackEntry
    val listaCompras by vModel.itensCarrinho.collectAsState()
    val totalGeral = listaCompras.sumOf { it.precoUnitario * it.quantidade }
    val erroMensagem by vModel.erroVenda.collectAsState()

    // Lógica para adicionar o item que acabou de chegar
    LaunchedEffect(navBackStackEntry) {
        val nome = navBackStackEntry?.arguments?.getString("produtoNome") ?: ""
        val preco = navBackStackEntry?.arguments?.getString("preco") ?: ""
        val qtd = navBackStackEntry?.arguments?.getInt("quantidade") ?: 1

        if (nome.isNotEmpty()) {
            vModel.adicionarAoCarrinho(nome, preco, qtd)
            navBackStackEntry?.arguments?.remove("produtoNome")
        }
    }

    var expandido by remember { mutableStateOf(false) }
    var formaPagamento by remember { mutableStateOf("Selecione...") }
    val opcoesPagamento = listOf("Pix", "Cartão", "Dinheiro")
    val corOuro = Color(0xFFC39953)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MEU CARRINHO", color = corOuro, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = corOuro)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
            )
        },
        // O segredo está aqui: o botão fica fixo no bottomBar
        bottomBar = {
            if (listaCompras.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            vModel.finalizarCompra(formaPagamento) {
                                // Esta lógica limpa TODAS as telas anteriores (carrinho, lista, etc)
                                // e volta o usuário para a página de categorias
                                navController.navigate("pagina_categorias") {
                                    popUpTo("home") { // Isso garante que ele não volte para telas proibidas
                                        inclusive = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        enabled = formaPagamento != "Selecione...",
                        colors = ButtonDefaults.buttonColors(containerColor = corOuro)
                    ) {
                        Text(
                            "FINALIZAR COMPRA - R$ ${String.format("%.2f", totalGeral)}",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("ITENS SELECIONADOS", color = corOuro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Lista de produtos
            items(listaCompras) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(item.nome, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("${item.quantidade}x R$ ${item.precoUnitario}", color = Color.Gray)
                        }
                        Text("R$ ${String.format("%.2f", item.precoUnitario * item.quantidade)}", color = corOuro, fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text("PAGAMENTO", color = corOuro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                // Menu de Pagamento
                ExposedDropdownMenuBox(
                    expanded = expandido,
                    onExpandedChange = { expandido = !expandido }
                ) {
                    OutlinedTextField(
                        value = formaPagamento,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = corOuro,
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandido,
                        onDismissRequest = { expandido = false },
                        modifier = Modifier.background(Color(0xFF1A1A1A))
                    ) {
                        opcoesPagamento.forEach { opcao ->
                            DropdownMenuItem(
                                text = { Text(opcao, color = Color.White) },
                                onClick = {
                                    formaPagamento = opcao
                                    expandido = false
                                }
                            )
                        }
                    }
                }

                if (erroMensagem.isNotEmpty()) {
                    Text(erroMensagem, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }

                // Espaço extra para o teclado ou scroll não cobrir o final
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}