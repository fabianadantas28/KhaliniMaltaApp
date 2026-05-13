package com.example.khalinimaltaapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.viewmodel.RelatoriosViewModel

// CORES OFICIAIS
val CorOuroPrincipalHome = Color(0xFFC79E5E)
val CardDarkBlueHome = Color(0xFF121A24)

@Composable
fun PaginaPrincipalKM(
    onAbrirMenu: () -> Unit,
    onIrParaLogin: () -> Unit,
    viewModel: RelatoriosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val totalClientes by viewModel.totalClientes.collectAsState(initial = 0)
    val valorTotalEstoque by viewModel.valorTotalEstoque.collectAsState(initial = 0.0)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo_logomarca),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(130.dp))

            Text(
                text = "Bem-vindo(a)! Gerencie suas\nvendas e estoque com facilidade.",
                color = CorOuroPrincipalHome,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Resumo do Dia",
                color = CorOuroPrincipalHome,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(15.dp))

            // SEÇÃO DE CARDS IGUAL À FIGURA
            // Resumo do Dia com design fiel à imagem
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Vendas Mês (Valor Real do Estoque)
                CardAtivoHome(
                    titulo = "Vendas\nMês",
                    valor = "R$ ${"%.2f".format(valorTotalEstoque)}",
                    modifier = Modifier.weight(1f)
                )

                // 2. Alertas Estoque (Seta para baixo em Dourado)
                CardAtivoHome(
                    titulo = "Alertas\nEstoque",
                    mostrarSeta = true, // Ativa a seta nativa
                    valor = "3",
                    modifier = Modifier.weight(1f)
                )

                // 3. Novos Clientes (Contagem Real)
                CardAtivoHome(
                    titulo = "Novos\nClientes",
                    valor = "$totalClientes",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

            Text(
                text = "Vendas Diárias na Última Semana",
                color = CorOuroPrincipalHome,
                fontSize = 13.sp
            )

            // GRÁFICO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(top = 10.dp)
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .border(1.dp, CorOuroPrincipalHome.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp, vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val vendasSemana = listOf(0.4f, 0.9f, 0.5f, 0.7f, 0.6f, 0.8f, 0.3f)
                    val dias = listOf("S", "T", "Q", "Q", "S", "S", "D")
                    vendasSemana.forEachIndexed { index, peso ->
                        Box(
                            modifier = Modifier
                                .width(10.dp)
                                .fillMaxHeight(peso)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = if (index == 1) listOf(CorOuroPrincipalHome, Color.White)
                                        else listOf(CorOuroPrincipalHome.copy(alpha = 0.3f), Color.Transparent)
                                    ),
                                    shape = RoundedCornerShape(50)
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(150.dp))
        }

        // BARRA INFERIOR
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            BotaoHome(icon = R.drawable.baseline_home_24, label = "Sair", aoClicar = onIrParaLogin)
            Spacer(modifier = Modifier.width(45.dp))
            BotaoHome(icon = android.R.drawable.ic_dialog_dialer, label = "Menu", aoClicar = onAbrirMenu)
        }
    }
}

@Composable
fun CardAtivoHome(titulo: String, valor: String, modifier: Modifier, mostrarSeta: Boolean = false) {
    Surface(
        modifier = modifier.height(100.dp),
        color = CardDarkBlueHome,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, CorOuroPrincipalHome.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = titulo, color = CorOuroPrincipalHome, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (mostrarSeta) {
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = CorOuroPrincipalHome, modifier = Modifier.size(20.dp))
                }
                Text(text = valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = if (valor.contains("R$")) 10.sp else 16.sp)
            }
        }
    }
}

@Composable
fun BotaoHome(icon: Int, label: String, aoClicar: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { aoClicar() }.padding(8.dp)) {
        Surface(modifier = Modifier.size(60.dp), shape = RoundedCornerShape(12.dp), color = CorOuroPrincipalHome) {
            Icon(painter = painterResource(id = icon), contentDescription = null, tint = Color.Black, modifier = Modifier.padding(16.dp))
        }
        Text(text = label, color = CorOuroPrincipalHome, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
    }
}