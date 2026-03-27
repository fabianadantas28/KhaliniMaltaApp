package com.example.khalinimaltaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CadastroClienteScreen(onContinuar: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var sobrenome by remember { mutableStateOf("") }
    var dataNasc by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var foneCelular by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var complemento by remember { mutableStateOf("") }
    var concordoLGPD by remember { mutableStateOf(false) }

    val corDourada = Color(0xFFFFD700)

    Box(modifier = Modifier.fillMaxSize()) {
        // FUNDO PRETO CORRETO (Sem a borda amarela fixa)
        Image(
            painter = painterResource(id = R.drawable.fundo_preto),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TÍTULO MAIS BAIXO (Para não grudar na logo do fundo)
            Spacer(modifier = Modifier.height(195.dp))

            Text(
                text = "Cadastro Cliente",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            CampoExterno("Nome", nome, { nome = it }, corDourada)
            CampoExterno("Sobrenome", sobrenome, { sobrenome = it }, corDourada)

            // LINHA COM DATA E CELULAR (Liberando espaço para o CPF)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) { CampoExterno("Data Nasc.", dataNasc, { dataNasc = it }, corDourada) }
                Box(Modifier.weight(1f)) { CampoExterno("Celular", foneCelular, { foneCelular = it }, corDourada) }
            }

            // CPF SOZINHO EMBAIXO (Máximo de espaço conforme solicitado)
            CampoExterno("CPF", cpf, { cpf = it }, corDourada)

            CampoExterno("E-mail", email, { email = it }, corDourada)

            // CHECKBOX LGPD
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = concordoLGPD,
                    onCheckedChange = { concordoLGPD = it },
                    colors = CheckboxDefaults.colors(checkedColor = corDourada, uncheckedColor = Color.White)
                )
                Text(
                    text = "Concordo em permitir o uso dos meus dados para comunicação sobre o status do pedido e promoções da loja (LGPD).",
                    color = Color.White,
                    fontSize = 11.sp
                )
            }

            CampoExterno("Complemento", complemento, { complemento = it }, corDourada, Modifier.height(55.dp))

            Spacer(modifier = Modifier.weight(1f))

            // BOTÃO MAIS ALTO (Para encaixar na moldura inferior)
            Button(
                onClick = onContinuar,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(46.dp)
                    .offset(y = (-45).dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("CONTINUAR PARA SENHA", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun CampoExterno(label: String, value: String, onValueChange: (String) -> Unit, cor: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(text = label, color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(start = 2.dp, bottom = 2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 14.sp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = cor,
                unfocusedBorderColor = cor
            )
        )
    }
}

// --- O PREVIEW QUE ESTAVA FALTANDO ---
@Preview(showSystemUi = true)
@Composable
fun PreviewCadastroCliente() {
    CadastroClienteScreen(onContinuar = {})
}