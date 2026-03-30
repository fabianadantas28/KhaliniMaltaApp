package com.example.khalinimaltaapp


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarSenhaScreen(onFinalizar: () -> Unit) {
    var nomeUsuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    val dourado = Color(0xFFD4AF37)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo_khalini),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Cadastro Senha",
                color = dourado,
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            CustomTextField(valor = nomeUsuario, aoMudar = { nomeUsuario = it }, label = "Nome Usuário", corDourada = dourado)
            Spacer(modifier = Modifier.height(15.dp))
            CustomTextField(valor = senha, aoMudar = { senha = it }, label = "Digite sua Senha", corDourada = dourado, isSenha = true)
            Spacer(modifier = Modifier.height(15.dp))
            CustomTextField(valor = confirmarSenha, aoMudar = { confirmarSenha = it }, label = "Confirmar Senha", corDourada = dourado, isSenha = true)

            Spacer(modifier = Modifier.height(40.dp))

            // --- BOTÃO DE CADASTRO COM CAIXA AMARELA E LETRA PRETA ---
            Button(
                onClick = onFinalizar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC79E5E), // Amarelo Bronzeado Exato
                    contentColor = Color.Black // Letra preta
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Cadastrar sua Conta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(valor: String, aoMudar: (String) -> Unit, label: String, corDourada: Color, isSenha: Boolean = false) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        label = { Text(label, color = corDourada) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White),
        visualTransformation = if (isSenha) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = corDourada,
            unfocusedBorderColor = corDourada,
            cursorColor = corDourada,
            focusedLabelColor = corDourada,
            unfocusedLabelColor = corDourada
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SenhaPreview() {
    CriarSenhaScreen(onFinalizar = {})
}