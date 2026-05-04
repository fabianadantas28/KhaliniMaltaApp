package com.example.khalinimaltaapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.viewmodel.RegistroVendaViewModel

// Cor única para o tema da tela
val CorDouradaVendaGeral = Color(0xFFC39953)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroVendaScreen(
    navController: NavController,
    vModel: RegistroVendaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Estados para controlar os campos de texto
    var buscaTexto by remember { mutableStateOf("") }
    var nomeClienteTexto by remember { mutableStateOf("") }
    var telefoneClienteTexto by remember { mutableStateOf("") }
    var formaPagamento by remember { mutableStateOf("Selecione...") }
    var expandirPagamento by remember { mutableStateOf(false) }

    // Estados observados da ViewModel
    val produtosSugeridos by vModel.produtosEncontrados.collectAsState()
    val vendaConfirmada by vModel.vendaConfirmada.collectAsState()
    val erroMensagem by vModel.erroVenda.collectAsState()

    var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }

    // Efeito para fechar a tela automaticamente quando a venda for confirmada no banco
    LaunchedEffect(vendaConfirmada) {
        if (vendaConfirmada) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Cabeçalho com botão de voltar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = CorDouradaVendaGeral)
            }
            Text(" Registro de Venda", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // --- BUSCA DE PRODUTO ---
        Text("Buscar Produto", color = CorDouradaVendaGeral, fontSize = 14.sp)
        OutlinedTextField(
            value = buscaTexto,
            onValueChange = {
                buscaTexto = it
                vModel.buscarProduto(it)
                if (it.isEmpty()) produtoSelecionado = null
            },
            placeholder = { Text("Digite o nome da joia...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = CorDouradaVendaGeral) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = CorDouradaVendaGeral,
                unfocusedContainerColor = Color(0xFF1A1A1A),
                focusedContainerColor = Color(0xFF1A1A1A)
            )
        )

        // Lista de sugestões de produtos (aparece enquanto digita)
        if (buscaTexto.isNotEmpty() && produtoSelecionado == null) {
            Card(
                modifier = Modifier.fillMaxWidth().heightIn(max = 150.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                border = BorderStroke(0.5.dp, Color.DarkGray)
            ) {
                LazyColumn {
                    items(produtosSugeridos) { produto ->
                        ListItem(
                            modifier = Modifier.clickable {
                                produtoSelecionado = produto
                                buscaTexto = produto.nomeProduto
                            },
                            headlineContent = { Text(produto.nomeProduto, color = Color.White) },
                            supportingContent = {
                                Text("Estoque: ${produto.qtdeEstoque} | R$ ${"%.2f".format(produto.preco)}", color = Color.Gray)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- DADOS DO CLIENTE ---
        CampoVendaAux("Nome do Cliente", Icons.Default.Person, nomeClienteTexto) { nomeClienteTexto = it }
        CampoVendaAux("WhatsApp", Icons.Default.Phone, telefoneClienteTexto) { telefoneClienteTexto = it }

        Spacer(modifier = Modifier.height(16.dp))

        // --- FORMA DE PAGAMENTO ---
        Text("Forma de Pagamento", color = CorDouradaVendaGeral, fontSize = 14.sp)
        Box {
            OutlinedButton(
                onClick = { expandirPagamento = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                border = BorderStroke(1.dp, Color.DarkGray)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(formaPagamento, color = Color.White)
                    Icon(Icons.Default.ArrowDropDown, null, tint = CorDouradaVendaGeral)
                }
            }
            DropdownMenu(
                expanded = expandirPagamento,
                onDismissRequest = { expandirPagamento = false }
            ) {
                listOf("Dinheiro", "Cartão de Crédito", "Cartão de Débito", "Pix").forEach { opcao ->
                    DropdownMenuItem(
                        text = { Text(opcao) },
                        onClick = {
                            formaPagamento = opcao
                            expandirPagamento = false
                        }
                    )
                }
            }
        }

        // Exibe erro se houver (ex: estoque insuficiente)
        if (erroMensagem.isNotEmpty()) {
            Text(erroMensagem, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- CARD DE TOTAL ---
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
            border = BorderStroke(1.dp, CorDouradaVendaGeral)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("TOTAL:", color = Color.White, fontWeight = FontWeight.Bold)
                val precoTotal = (produtoSelecionado?.preco ?: 0.0)
                Text("R$ ${"%.2f".format(precoTotal)}", color = CorDouradaVendaGeral, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }

        // --- BOTÃO CONFIRMAR ---
        Button(
            onClick = {
                produtoSelecionado?.let { prod ->
                    vModel.confirmarVenda(
                        produto = prod,
                        quantidade = 1,
                        nomeCliente = nomeClienteTexto,
                        telefoneCliente = telefoneClienteTexto,
                        formaPagamento = formaPagamento
                    )
                }
            },
            enabled = produtoSelecionado != null && formaPagamento != "Selecione...",
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CorDouradaVendaGeral),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("CONFIRMAR VENDA", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CampoVendaAux(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    valor: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        leadingIcon = { Icon(icon, null, tint = CorDouradaVendaGeral) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = CorDouradaVendaGeral,
            unfocusedBorderColor = Color.DarkGray,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    )
}