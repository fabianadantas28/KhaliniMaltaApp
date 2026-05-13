package com.example.khalinimaltaapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.khalinimaltaapp.viewmodel.CadastroProdutoViewModel

val CorOuroEnvelhecido = Color(0xFFC79E5E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoProduto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
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
            readOnly = readOnly,
            trailingIcon = trailingIcon,
            textStyle = TextStyle(color = Color.White),
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
fun CadastroProdutoScreen(navController: NavController, viewModel: CadastroProdutoViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val gap = 4.dp

    val categoriasOficiais = listOf(
        "Anéis", "Colares", "Brincos", "Pulseiras", "Tornozeleiras", "Acessórios"
    )
    var menuExpandido by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo_logomarca),
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
            // AJUSTE AQUI: Aumentei para 80.dp para a seta descer e sair da barra de status
            Spacer(modifier = Modifier.height(80.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(48.dp) // Área de toque maior e mais confortável
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = CorOuroEnvelhecido,
                        modifier = Modifier.size(32.dp) // Seta um pouco mais visível
                    )
                }
            }

            // Espaço entre a seta e o título "Cadastro Produto"
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Cadastro Produto",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

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

            CampoProduto("Nome Produto", viewModel.nome, { viewModel.nome = it })

            Spacer(modifier = Modifier.height(gap))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoProduto("Marca", viewModel.marca, { viewModel.marca = it }, Modifier.weight(1f))
                CampoProduto("Material", viewModel.material, { viewModel.material = it }, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(gap))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoProduto("Código Interno", viewModel.codigo, { viewModel.codigo = it }, Modifier.weight(1f))
                CampoProduto(
                    label = "Preço (R$)",
                    value = viewModel.preco,
                    onValueChange = { viewModel.preco = it },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(gap))

            CampoProduto("Descrição", viewModel.descricao, { viewModel.descricao = it }, Modifier.height(90.dp))

            Spacer(modifier = Modifier.height(gap))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Bottom) {
                Box(modifier = Modifier.weight(1.5f)) {
                    CampoProduto(
                        label = "Categoria",
                        value = viewModel.categoria,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { menuExpandido = true }) {
                                Icon(
                                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                                    contentDescription = null,
                                    tint = CorOuroEnvelhecido
                                )
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        categoriasOficiais.forEach { nomeCat ->
                            DropdownMenuItem(
                                text = { Text(nomeCat) },
                                onClick = {
                                    viewModel.categoria = nomeCat
                                    menuExpandido = false
                                }
                            )
                        }
                    }
                }

                CampoProduto(
                    label = "Qtde",
                    value = viewModel.estoque,
                    onValueChange = { viewModel.estoque = it },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.salvarProduto(
                        onSucesso = {
                            Toast.makeText(context, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
                            navController.navigate("controle_estoque") {
                                popUpTo("cadastro_produto") { inclusive = true }
                            }
                        },
                        onError = { erro ->
                            Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = CorOuroEnvelhecido),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Cadastrar", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}