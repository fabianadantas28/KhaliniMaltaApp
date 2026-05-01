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

// Cor única para evitar erro de "Conflicting declarations"
val CorDouradaVenda = Color(0xFFC39953)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroVendaScreen(
    navController: NavController,
    vModel: RegistroVendaViewModel
) {
    var buscaTexto by remember { mutableStateOf("") }
    var nomeCliente by remember { mutableStateOf("") }
    var telefoneCliente by remember { mutableStateOf("") }
    var formaPagamento by remember { mutableStateOf("Selecione...") }
    var expandirPagamento by remember { mutableStateOf(false) }

    val produtosSugeridos by vModel.produtosEncontrados.collectAsState()
    var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Cabeçalho
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = CorDouradaVenda)
            }
            Text(" Registro de Venda", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // Busca
        Text("Buscar Produto", color = CorDouradaVenda, fontSize = 14.sp)
        OutlinedTextField(
            value = buscaTexto,
            onValueChange = {
                buscaTexto = it
                vModel.buscarProduto(it)
                if (it.isEmpty()) produtoSelecionado = null
            },
            placeholder = { Text("Digite o nome da joia...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = CorDouradaVenda) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = CorDouradaVenda,
                unfocusedContainerColor = Color(0xFF1A1A1A),
                focusedContainerColor = Color(0xFF1A1A1A)
            )
        )

        // Lista de Sugestões
        if (buscaTexto.isNotEmpty() && produtoSelecionado == null) {
            Card(
                modifier = Modifier.fillMaxWidth().heightIn(max = 150.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
            ) {
                LazyColumn {
                    items(produtosSugeridos) { produto ->
                        ListItem(
                            modifier = Modifier.clickable {
                                produtoSelecionado = produto
                                buscaTexto = produto.nomeProduto
                            },
                            headlineContent = { Text(produto.nomeProduto, color = Color.White) },
                            supportingContent = { Text("Estoque: ${produto.qtdeEstoque} | R$ ${produto.preco}", color = Color.Gray) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dados do Cliente
        CampoVendaAux("Nome do Cliente", Icons.Default.Person, nomeCliente) { nomeCliente = it }
        CampoVendaAux("WhatsApp", Icons.Default.Phone, telefoneCliente) { telefoneCliente = it }

        Spacer(modifier = Modifier.height(16.dp))

        // Pagamento
        Text("Forma de Pagamento", color = CorDouradaVenda, fontSize = 14.sp)
        Box {
            OutlinedButton(
                onClick = { expandirPagamento = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                border = BorderStroke(1.dp, Color.DarkGray)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(formaPagamento, color = Color.White)
                    Icon(Icons.Default.ArrowDropDown, null, tint = CorDouradaVenda)
                }
            }
            DropdownMenu(expanded = expandirPagamento, onDismissRequest = { expandirPagamento = false }) {
                listOf("Dinheiro", "Cartão", "Pix").forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = {
                        formaPagamento = it
                        expandirPagamento = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Total e Botão
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
            border = BorderStroke(1.dp, CorDouradaVenda)
        ) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("TOTAL:", color = Color.White, fontWeight = FontWeight.Bold)
                Text("R$ ${produtoSelecionado?.preco ?: 0.0}", color = CorDouradaVenda, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }

        Button(
            onClick = {
                produtoSelecionado?.let { vModel.confirmarVenda(it, 1) }
                navController.popBackStack()
            },
            enabled = produtoSelecionado != null && formaPagamento != "Selecione...",
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CorDouradaVenda),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("CONFIRMAR VENDA", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CampoVendaAux(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, valor: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        leadingIcon = { Icon(icon, null, tint = CorDouradaVenda) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
    )
}