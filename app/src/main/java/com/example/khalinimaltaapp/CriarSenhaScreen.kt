package com.example.khalinimaltaapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CriarSenhaScreen(onFinalizar: () -> Unit) {
    // Por enquanto, apenas um texto para o erro sumir
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Tela de Criação de Senha", color = Color.Gray)
            Button(onClick = onFinalizar) {
                Text("Finalizar")
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SenhaPreview() {
    CriarSenhaScreen(onFinalizar = {})
}