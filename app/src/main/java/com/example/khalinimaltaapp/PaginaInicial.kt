package com.example.khalinimaltaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Cor Ouro Envelhecido da Khalini Malta (Usaremos este nome em tudo agora)
val CorOuroKhalini = Color(0xFFC79E5E)

@Composable
fun PaginaPrincipalKM() {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Fundo que preenche toda a tela
        Image(
            painter = painterResource(id = R.drawable.fundo_khalini),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Conteúdo Principal dentro da Moldura
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Espaço para a logomarca que fica no topo
            Spacer(modifier = Modifier.height(199.dp))

            Text(
                text = "Bem-vindo(a)! Gerencie suas\nvendas e estoque com facilidade.",
                color = CorOuroKhalini,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Resumo do Dia",
                color = CorOuroKhalini,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(25.dp))

            // 2. Linha dos Cards de Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CardStatus(titulo = "Vendas Mês", valor = "$")
                CardStatus(titulo = "Alertas", iconRes = R.drawable.outline_check_alert_24)
                CardStatus(titulo = "Novos Clientes", iconRes = R.drawable.outline_demography_24)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Vendas Diárias na Última Semana",
                color = CorOuroKhalini,
                fontSize = 13.sp
            )

            // 3. Área do Gráfico (CORRIGIDO: mudei 'dourado' para 'CorOuroKhalini')
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(160.dp)
                    .padding(top = 8.dp)
                    .border(1.dp, CorOuroKhalini, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.BottomCenter // Alinha as barras na base
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val vendasSemana = listOf(0.4f, 0.7f, 0.5f, 0.9f, 0.6f, 0.8f, 0.3f)
                    val dias = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sab", "Dom")

                    vendasSemana.forEachIndexed { index, altura ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .fillMaxHeight(altura * 0.8f) // Multipliquei por 0.8 para não bater no teto
                                    .background(CorOuroKhalini, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            )
                            Text(
                                text = dias[index].first().toString(),
                                color = CorOuroKhalini, // CORREÇÃO AQUI: era dourado, agora é CorOuroKhalini
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(150.dp))
        }

        // 4. Botões Inferiores
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotaoInferior(icon = R.drawable.baseline_home_24, label = "Início")
            Spacer(modifier = Modifier.width(45.dp))
            BotaoInferior(icon = android.R.drawable.ic_dialog_dialer, label = "Menu")
        }
    }
}

@Composable
fun CardStatus(titulo: String, valor: String? = null, iconRes: Int? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = titulo, color = CorOuroKhalini, fontSize = 10.sp, modifier = Modifier.padding(bottom = 4.dp))
        Surface(
            modifier = Modifier.size(75.dp),
            shape = RoundedCornerShape(10.dp),
            color = CorOuroKhalini
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (valor != null) {
                    Text(text = valor, color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                } else if (iconRes != null) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BotaoInferior(icon: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(65.dp),
            shape = RoundedCornerShape(12.dp),
            color = CorOuroKhalini
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
        }
        Text(text = label, color = CorOuroKhalini, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPaginaPrincipalKM() {
    PaginaPrincipalKM()
}