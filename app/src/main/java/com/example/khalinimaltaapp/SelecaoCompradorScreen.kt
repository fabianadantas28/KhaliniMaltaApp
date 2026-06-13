package com.example.khalinimaltaapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelecaoCompradorScreen(
    onSelecionar: (String) -> Unit, // Agora recebe o texto do perfil escolhido
    onCancelar: () -> Unit
) {
    val corOuro = Color(0xFFC79E5E)

    Box(modifier = Modifier.fillMaxSize()) {
        // Mantém a identidade visual com o fundo padrão do seu app
        Image(
            painter = painterResource(id = R.drawable.fundo_logomarca),
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
            Text(
                text = "QUEM ESTÁ COMPRANDO?",
                color = corOuro,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selecione o perfil para diferenciar a venda no recibo e nos relatórios de faturamento.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Opção 1: Cliente Comum
            Button(
                onClick = { onSelecionar("Cliente Balcão") },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF121A24).copy(alpha = 0.85f)),
                border = BorderStroke(1.dp, corOuro),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cliente Comum (Balcão)", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Opção 2: Funcionário
            Button(
                onClick = { onSelecionar("Consumo Funcionário") },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF121A24).copy(alpha = 0.85f)),
                border = BorderStroke(1.dp, corOuro),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Funcionário da Loja", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Opção 3: Administrador
            Button(
                onClick = { onSelecionar("Uso do Administrador") },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF121A24).copy(alpha = 0.85f)),
                border = BorderStroke(1.dp, corOuro),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Administrador / Retirada", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botão Cancelar/Voltar
            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier.fillMaxWidth(0.6f).height(45.dp),
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("CANCELAR", fontSize = 14.sp)
            }
        }
    }
}