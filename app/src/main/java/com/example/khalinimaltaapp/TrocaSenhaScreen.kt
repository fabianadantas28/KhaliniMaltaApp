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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.viewmodel.TrocaSenhaViewModel

@Composable
fun TrocaSenhaScreen(
    usuarioId: Int,
    onSenhaAtualizada: () -> Unit,
    viewModel: TrocaSenhaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val dourado = Color(0xFFC79E5E)

    // Navega para home após troca bem-sucedida
    LaunchedEffect(viewModel.trocouComSucesso) {
        if (viewModel.trocouComSucesso) {
            onSenhaAtualizada()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo_preto),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ícone de aviso
            Text("🔐", fontSize = 48.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Primeiro Acesso",
                color = dourado,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Por segurança, você precisa\nredefinir sua senha antes de continuar.",
                color = Color.White,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Nova senha
            OutlinedTextField(
                value = viewModel.novaSenha,
                onValueChange = { viewModel.novaSenha = it },
                label = { Text("Nova senha", color = dourado) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = dourado,
                    unfocusedBorderColor = dourado,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirmar nova senha
            OutlinedTextField(
                value = viewModel.confirmarSenha,
                onValueChange = { viewModel.confirmarSenha = it },
                label = { Text("Confirmar nova senha", color = dourado) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = dourado,
                    unfocusedBorderColor = dourado,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Mensagem de erro
            if (viewModel.mensagemErro.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = viewModel.mensagemErro,
                    color = Color.Red,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.trocarSenha(usuarioId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = dourado,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Salvar Nova Senha", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
