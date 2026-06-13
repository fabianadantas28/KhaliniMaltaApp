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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.khalinimaltaapp.viewmodel.LoginViewModel
import com.example.khalinimaltaapp.viewmodel.CadastroClienteViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    sharedViewModel: CadastroClienteViewModel,
    onIrParaCadastro: () -> Unit,
    onIrParaPaginaInicial: () -> Unit,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val dourado = Color(0xFFC79E5E)

    // 1. Caso precise trocar a senha
    LaunchedEffect(viewModel.irParaTrocaSenha) {
        if (viewModel.irParaTrocaSenha) {
            val id = viewModel.usuarioLogado?.id ?: 0
            navController.navigate("troca_senha/$id")
            viewModel.irParaTrocaSenha = false
        }
    }

    // 2. Direcionamento Inteligente
    LaunchedEffect(viewModel.irParaHome) {
        if (viewModel.irParaHome) {
            if (viewModel.tipoUsuarioLogado == "ADMIN" || viewModel.tipoUsuarioLogado == "FUNCIONARIO") {
                onIrParaPaginaInicial()
            } else {
                navController.navigate("pagina_categorias") {
                    popUpTo("login") { inclusive = true }
                }
            }
            viewModel.irParaHome = false
        }
    }

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
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(160.dp))

            // Campo Usuário
            OutlinedTextField(
                value = viewModel.usuario,
                onValueChange = { viewModel.usuario = it },
                label = { Text("Usuário/E-mail", color = dourado) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = dourado,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Senha
            OutlinedTextField(
                value = viewModel.senha,
                onValueChange = { viewModel.senha = it },
                label = { Text("Senha", color = dourado) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = dourado,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            if (viewModel.loginErro) {
                Text(
                    text = "Usuário ou senha incorretos",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            TextButton(
                onClick = { viewModel.mostrarDialogo = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Esqueci minha senha", color = Color.White, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- SUBSTITUA APENAS O BOTÃO ENTRAR NA SUA LOGINSCREEN.KT ---

            Button(
                onClick = {
                    // Chamada direta e limpa sem o try/catch que mascarava o erro
                    viewModel.fazerLogin(sharedViewModel)
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

            Text(text = "Ainda não tem conta?", color = dourado, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(10.dp))

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

    if (viewModel.mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { viewModel.mostrarDialogo = false },
            confirmButton = {
                TextButton(onClick = { viewModel.mostrarDialogo = false }) {
                    Text("OK", color = dourado)
                }
            },
            title = { Text("Recuperação de Senha", color = Color.White) },
            text = { Text("Um link de redefinição foi enviado para o e-mail cadastrado.", color = Color.White) },
            containerColor = Color(0xFF1A1A1A),
            titleContentColor = dourado
        )
    }
}