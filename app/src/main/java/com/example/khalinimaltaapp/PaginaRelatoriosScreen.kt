package com.example.khalinimaltaapp.ui.relatorio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Cores do Projeto
val KhaliniGold = Color(0xFFC39953)
val CardDarkBlue = Color(0xFF121A24)

@Composable
fun PaginaRelatorio() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        HeaderRelatorio()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "▽ Filtros do Relatório",
            color = KhaliniGold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FiltrosRelatorio()

        Spacer(modifier = Modifier.height(24.dp))

        ResumoCards()

        Spacer(modifier = Modifier.height(24.dp))

        GraficoVendas()

        Spacer(modifier = Modifier.weight(1f))

        BotoesRelatorio()

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("● Backup diário: 04/10/2025", color = Color.Gray, fontSize = 10.sp)
            Text("Atualizado: 17:54", color = Color.Gray, fontSize = 10.sp)
        }
    }
}

@Composable
fun HeaderRelatorio() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícone universal de voltar (ArrowBack)
        Icon(Icons.Default.ArrowBack, "Voltar", tint = KhaliniGold)

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Trocado Assessment por Info (Universal)
            Icon(Icons.Default.Info, null, tint = KhaliniGold, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
            Text("Relatórios", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Icon(Icons.Default.Refresh, "Atualizar", tint = KhaliniGold)
    }
}

@Composable
fun FiltrosRelatorio() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(
            onClick = {},
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.dp, KhaliniGold),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = CardDarkBlue)
        ) {
            // Trocado DateRange por DateRange (Universal)
            Icon(Icons.Default.DateRange, null, tint = KhaliniGold, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Últimos 7 dias", color = Color.White, fontSize = 11.sp)
        }

        OutlinedButton(
            onClick = {},
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.dp, Color.DarkGray),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = CardDarkBlue)
        ) {
            Text("Relatório de Vendas", color = Color.White, fontSize = 11.sp)
        }
    }
}

@Composable
fun ResumoCards() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CardInfo("Receita Total", "R$ 3.220,00", Modifier.weight(1.1f))
        CardInfo("Total Vendas", "23", Modifier.weight(0.9f))
        CardInfo("Ticket Médio", "R$ 140,00", Modifier.weight(1f))
    }
}

@Composable
fun CardInfo(titulo: String, valor: String, modifier: Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBlue),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(titulo, color = Color.Gray, fontSize = 11.sp)
            Spacer(Modifier.height(8.dp))
            Text(valor, color = if(valor.contains("R$")) KhaliniGold else Color.White,
                fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Composable
fun GraficoVendas() {
    Card(
        modifier = Modifier.fillMaxWidth().height(250.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBlue),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Evolução de Vendas", color = KhaliniGold, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                val alturas = listOf(0.5f, 0.5f, 0.3f, 0.6f, 0.55f, 0.7f)
                val meses = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun")

                alturas.forEachIndexed { index, altura ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .fillMaxHeight(altura)
                                .background(KhaliniGold, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(meses[index], color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BotoesRelatorio() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(48.dp),
            border = BorderStroke(1.dp, KhaliniGold),
            shape = RoundedCornerShape(8.dp)
        ) {
            // Trocado Assessment por Build (Universal) que simboliza gerar/construir
            Icon(Icons.Default.Build, null, tint = KhaliniGold)
            Spacer(Modifier.width(8.dp))
            Text("Gerar Relatório", color = KhaliniGold)
        }

        OutlinedButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(48.dp),
            border = BorderStroke(1.dp, KhaliniGold),
            shape = RoundedCornerShape(8.dp)
        ) {
            // Trocado EditNote por Share (Universal) para exportar
            Icon(Icons.Default.Share, null, tint = KhaliniGold)
            Spacer(Modifier.width(8.dp))
            Text("Exportar PDF", color = KhaliniGold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRelatorio() {
    PaginaRelatorio()
}