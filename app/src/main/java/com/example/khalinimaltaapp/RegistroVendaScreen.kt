package com.example.khalinimaltaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    vModel: RegistroVendaViewModel,
    produtoNome: String = "", // Recebe automático da Categoria
    produtoPreco: String = "" // Recebe automático da Categoria
) {
    // 1. Estados dos Campos (O nome do cliente já simulando o Login)
    var nomeClienteTexto by remember { mutableStateOf("Cliente Logado") }

    // 2. Estados da Forma de Pagamento
    var expandido by remember { mutableStateOf(false) }
    var formaPagamento by remember { mutableStateOf("Selecione...") }
    val opcoesPagamento = listOf("Pix", "Cartão de Crédito", "Cartão de Débito", "Dinheiro")

    // 3. Estados do Cartão
    var numeroCartao by remember { mutableStateOf("") }
    var validadeCartao by remember { mutableStateOf("") }
    var cvvCartao by remember { mutableStateOf("") }

    val corOuro = Color(0xFFC39953)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Espaço para a Logomarca não cobrir o título
        Spacer(modifier = Modifier.height(110.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = corOuro)
            }
            Text("FINALIZAR COMPRA", color = corOuro, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 1. DADOS DO CLIENTE ---
        Text("CLIENTE", color = corOuro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = nomeClienteTexto,
            onValueChange = { nomeClienteTexto = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true, // Cliente não altera o nome se já estiver logado
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = corOuro
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- 2. PRODUTO SELECIONADO (Vindo automaticamente) ---
        Text("ITEM SELECIONADO", color = corOuro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
            border = androidx.compose.foundation.BorderStroke(1.dp, corOuro.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = produtoNome, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = "Unidade", color = Color.Gray, fontSize = 12.sp)
                }
                Text(text = "R$ $produtoPreco", color = corOuro, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- 3. FORMA DE PAGAMENTO (Caixinha Selecionável) ---
        Text("FORMA DE PAGAMENTO", color = corOuro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido }
        ) {
            OutlinedTextField(
                value = formaPagamento,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = corOuro
                )
            )

            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opcoesPagamento.forEach { opcao ->
                    DropdownMenuItem(
                        text = { Text(opcao) },
                        onClick = {
                            formaPagamento = opcao
                            expandido = false
                        }
                    )
                }
            }
        }

        // --- 4. DADOS DO CARTÃO (Só aparecem se escolher cartão) ---
        if (formaPagamento.contains("Cartão")) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("INFORMAÇÕES DO CARTÃO", color = corOuro, fontSize = 12.sp)

            OutlinedTextField(
                value = numeroCartao,
                onValueChange = { numeroCartao = it },
                label = { Text("Número do Cartão") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = validadeCartao,
                    onValueChange = { validadeCartao = it },
                    label = { Text("Validade") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                OutlinedTextField(
                    value = cvvCartao,
                    onValueChange = { cvvCartao = it },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(0.5f),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // --- 5. BOTÃO FINALIZAR ---
        Button(
            onClick = {
                // Aqui o ViewModel recebe o comando de venda
                // vModel.confirmarVenda(...)
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            enabled = (formaPagamento != "Selecione..."),
            colors = ButtonDefaults.buttonColors(containerColor = corOuro)
        ) {
            Text("FINALIZAR COMPRA", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}