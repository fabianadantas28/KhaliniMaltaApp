package com.example.khalinimaltaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Cor Ouro da Khalini Malta
val CorOuroEnvelhecido = Color(0xFFC79E5E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoProduto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 2.dp, start = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = CorOuroEnvelhecido,
                unfocusedBorderColor = CorOuroEnvelhecido.copy(alpha = 0.7f),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun CadastroProdutoScreen() {
    val scrollState = rememberScrollState()

    // Estados para os campos de texto
    var nome by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var estoque by remember { mutableStateOf("") }

    // Gap reduzido para 4dp para que categoria/qtde fiquem bem próximas das de cima
    val gap = 4.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagem de Fundo (Logomarca KM)
        Image(
            painter = painterResource(id = R.drawable.fundo_preto),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Espaço para a logo circular que já está no fundo
            Spacer(modifier = Modifier.height(195.dp))

            // Título centralizado abaixo da logo
            Text(
                text = "Cadastro Produto",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // 2. Foto posicionada à direita
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(85.dp)
                        .border(1.5.dp, CorOuroEnvelhecido, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Foto", color = CorOuroEnvelhecido, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(gap))

            // 3. Bloco de campos com espaçamento unificado
            CampoProduto("Nome Produto", nome, { nome = it })
            Spacer(modifier = Modifier.height(gap))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoProduto("Marca", marca, { marca = it }, Modifier.weight(1f))
                CampoProduto("Material", material, { material = it }, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(gap))

            CampoProduto("Código Interno", codigo, { codigo = it })
            Spacer(modifier = Modifier.height(gap))

            // Caixa de descrição com altura fixa
            CampoProduto("Descrição", descricao, { descricao = it }, Modifier.height(85.dp))

            // Espaçamento idêntico para Categoria e Qtde subirem
            Spacer(modifier = Modifier.height(gap))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoProduto("Categoria", categoria, { categoria = it }, Modifier.weight(1.5f))
                CampoProduto(
                    label = "Qtde",
                    value = estoque,
                    onValueChange = { estoque = it },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            // 4. Botão Cadastrar posicionado logo abaixo (sem vãos grandes)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Ação futuro */ },
                colors = ButtonDefaults.buttonColors(containerColor = CorOuroEnvelhecido),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Cadastrar", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            // Espaço final para o scroll não cortar o botão
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCadastroFinalCompacto() {
    CadastroProdutoScreen()
}