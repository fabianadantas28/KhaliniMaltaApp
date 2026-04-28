package com.example.khalinimaltaapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.viewmodel.CadastroClienteViewModel

@Composable
fun CadastroClienteScreen(
    onContinuar: () -> Unit,
    viewModel: CadastroClienteViewModel
) {
    val corDourada = Color(0xFFC79E5E)
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var concordouLGPD by remember { mutableStateOf(false) }
    // Dica: Se quiser que o complemento salve no ViewModel, use viewModel.complemento
    var complementoLocal by remember { mutableStateOf("") }

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
                .padding(horizontal = 28.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. REDUZIDO O TOPO: De 195.dp para 80.dp para dar espaço à logomarca
            Spacer(modifier = Modifier.height(120.dp))

            Text(
                text = "Cadastro Cliente",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 2. CAMPOS MAIS COMPACTOS: Espaçamento vertical reduzido
            CampoExterno("Nome", viewModel.nome, { viewModel.nome = it }, corDourada)
            CampoExterno("Sobrenome", viewModel.sobrenome, { viewModel.sobrenome = it }, corDourada)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) {
                    CampoExterno("Data Nasc.", viewModel.dataNasc, { viewModel.dataNasc = it }, corDourada)
                }
                Box(Modifier.weight(1f)) {
                    CampoExterno("Celular", viewModel.foneCelular, { viewModel.foneCelular = it }, corDourada)
                }
            }

            CampoExterno("CPF", viewModel.cpf, { viewModel.cpf = it }, corDourada)
            CampoExterno("E-mail", viewModel.email, { viewModel.email = it }, corDourada)
            CampoExterno("Complemento", complementoLocal, { complementoLocal = it }, corDourada)

            Spacer(modifier = Modifier.height(10.dp))

            // 3. LGPD MAIS DISCRETA
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = concordouLGPD,
                    onCheckedChange = { concordouLGPD = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = corDourada,
                        uncheckedColor = corDourada,
                        checkmarkColor = Color.Black
                    ),
                    modifier = Modifier.scale(0.8f) // Reduz um pouco o tamanho do quadrado
                )
                Text(
                    text = "Concordo em permitir o uso dos meus dados para comunicação sobre o status do pedido e promoções da loja (LGPD).",
                    color = Color.White,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (concordouLGPD) {
                        onContinuar()
                    } else {
                        Toast.makeText(context, "Aceite os termos da LGPD!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = corDourada),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "CONTINUAR PARA SENHA",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            // Espaço para não grudar no fundo ao rolar
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoExterno(label: String, value: String, onValueChange: (String) -> Unit, cor: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 2.dp)) { // Padding reduzido de 4 para 2
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 1.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(44.dp), // Altura reduzida de 48 para 44
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 13.sp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = cor,
                unfocusedBorderColor = cor,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = cor
            )
        )
    }
}