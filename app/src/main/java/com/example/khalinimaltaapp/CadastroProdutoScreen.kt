package com.example.khalinimaltaapp

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.khalinimaltaapp.data.Produto
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import kotlinx.coroutines.launch

// 1. DEFINIÇÃO DA COR
val CorOuroEnvelhecido = Color(0xFFC79E5E)

// 2. FUNÇÃO AUXILIAR DO CAMPO
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

// 3. TELA PRINCIPAL
@Composable
fun CadastroProdutoScreen(navController: NavController, produtoDao: ProdutoDao) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Estados dos campos
    var nome by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") } // NOVO CAMPO ADICIONADO
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var estoque by remember { mutableStateOf("") }

    val gap = 4.dp

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
            Spacer(modifier = Modifier.height(150.dp))

            Text(
                text = "Cadastro Produto",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // Espaço para Foto
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

            CampoProduto("Nome Produto", nome, { nome = it })
            Spacer(modifier = Modifier.height(gap))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoProduto("Marca", marca, { marca = it }, Modifier.weight(1f))
                CampoProduto("Material", material, { material = it }, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(gap))

            // Linha com Código e Preço
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoProduto("Código Interno", codigo, { codigo = it }, Modifier.weight(1f))
                CampoProduto(
                    label = "Preço (R$)",
                    value = preco,
                    onValueChange = { preco = it },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(gap))

            CampoProduto("Descrição", descricao, { descricao = it }, Modifier.height(90.dp))
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nome.isBlank() || categoria.isBlank() || preco.isBlank()) {
                        Toast.makeText(context, "Preencha Nome, Categoria e Preço!", Toast.LENGTH_SHORT).show()
                    } else {
                        scope.launch {
                            try {
                                // Criando o produto com os valores convertidos
                                val novoProduto = Produto(
                                    nomeProduto = nome,
                                    marca = marca,
                                    material = material,
                                    codigoInterno = codigo,
                                    descricao = descricao,
                                    categoria = categoria,
                                    qtdeEstoque = estoque.toIntOrNull() ?: 0,
                                    preco = preco.replace(",", ".").toDoubleOrNull() ?: 0.0, // Converte preço para Double
                                    imagemUrl = "" // String vazia para não dar erro
                                )

                                produtoDao.inserir(novoProduto)

                                Toast.makeText(context, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                navController.navigateUp()

                            } catch (e: Exception) {
                                Log.e("ERRO_CADASTRO", "Erro ao salvar: ${e.message}")
                                Toast.makeText(context, "Erro ao salvar no banco!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
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