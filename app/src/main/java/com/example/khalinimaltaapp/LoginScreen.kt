package com.example.khalinimaltaapp

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onIrParaCadastro: () -> Unit,
    onIrParaPaginaInicial: () -> Unit // <-- 1. LINHA ADICIONADA
) {
    // Definição da cor dourada padrão da marca
    val dourado = Color(0xFFC79E5E)

    // Estados para os campos de texto
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // FUNDO KHALINI
        Image(
            painter = painterResource(id = R.drawable.fundo_khalini),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(160.dp))

            // Campo de Usuário
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuário", color = dourado) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = dourado,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = dourado,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha", color = dourado) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = dourado,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = dourado,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Link Esqueci Senha
            // Procure por esta parte no seu código:
            TextButton(
                onClick = {
                    mostrarDialogo = true // <--- Muda para verdadeiro para o aviso aparecer
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Esqueci minha senha", color = Color.White, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botão Entrar
            Button(
                onClick = {
                    onIrParaPaginaInicial() // <-- 2. COLOQUEI A AÇÃO DE CLIQUE AQUI
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = dourado),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("ENTRAR", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Seção de Cadastro
            Text(
                text = "Ainda não tem conta?",
                color = dourado,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Botão para navegar até o Cadastro
            OutlinedButton(
                onClick = onIrParaCadastro,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                border = BorderStroke(1.dp, dourado),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = dourado)
            ) {
                Text("Cadastre-se", fontSize = 18.sp)
            }
        }
    }
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            confirmButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("OK", color = dourado)
                }
            },
            title = { Text("Recuperação de Senha", color = Color.White) },
            text = { Text("Um link de redefinição foi enviado para o e-mail cadastrado.", color = Color.White) },
            containerColor = Color(0xFF1A1A1A), // Um cinza bem escuro para combinar com o fundo
            titleContentColor = dourado
        )
    }
}

// Preview atualizado para não dar erro
@Preview(showSystemUi = true)
@Composable
fun PreviewLogin() {
    LoginScreen(
        onIrParaCadastro = {},
        onIrParaPaginaInicial = {} // <-- 3. AJUSTEI O PREVIEW AQUI TAMBÉM
    )
}
