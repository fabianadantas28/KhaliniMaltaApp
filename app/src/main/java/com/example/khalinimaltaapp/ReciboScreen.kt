package com.example.khalinimaltaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.data.Venda

@Composable
fun ReciboScreen(
    venda: Venda,
    onFinalizar: () -> Unit
) {
    val dourado = Color(0xFFC79E5E)

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícone de Sucesso
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(80.dp)
            )

            Text(
                text = "Pedido Realizado!",
                color = dourado,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Cartão do Recibo
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("CLIENTE: ${venda.nomeCliente.uppercase()}", color = Color.White, fontWeight = FontWeight.Bold)

                    @OptIn(ExperimentalMaterial3Api::class)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray)

                    // Detalhes da Compra
                    Text("PRODUTO: ${venda.nomeProduto}", color = Color.LightGray)
                    Text("QUANTIDADE: ${venda.quantidade}", color = Color.LightGray)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("FORMA DE PAGTO:", color = Color.LightGray)
                        Text(venda.formaPagamento, color = dourado, fontWeight = FontWeight.Bold)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("TOTAL:", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        // LINHA CORRIGIDA CONFORME SUGESTÃO DO JOÃO:
                        Text(
                            text = "R$ ${String.format("%.2f", venda.valorTotal)}",
                            color = dourado,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onFinalizar,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = dourado)
            ) {
                Text("CONCLUÍDO", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}