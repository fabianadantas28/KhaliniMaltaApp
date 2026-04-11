package com.example.khalinimaltaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. DEFINIÇÃO DO MODELO (Tem que estar aqui para não dar erro de referência)
data class MenuItemData(
    val titulo: String,
    val icone: ImageVector,
    val rota: String
)

val CorOuroMenuFixo = Color(0xFFC79E5E)

@Composable
fun MenuScreen(onVoltar: () -> Unit) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(210.dp))

            Text(
                text = "MENU PRINCIPAL",
                color = CorOuroMenuFixo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            // GRID DE OPÇÕES
            Box(modifier = Modifier.weight(1f)) {
                MenuGrid()
            }

            // BOTÃO VOLTAR
            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(45.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorOuroMenuFixo),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("VOLTAR AO INÍCIO", color = CorOuroMenuFixo, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

@Composable
fun MenuGrid() {
    val itens = listOf(
        MenuItemData("Clientes", Icons.Default.Person, "clientes"),
        MenuItemData("Produtos", Icons.Default.ShoppingCart, "produtos"),
        MenuItemData("Vendas", Icons.Default.ThumbUp, "vendas"),
        MenuItemData("Relatórios", Icons.Default.Info, "relatorios"),
        MenuItemData("Estoque", Icons.Default.Build, "estoque")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(itens) { item ->
            MenuCard(item)
        }
    }
}

@Composable
fun MenuCard(item: MenuItemData) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { /* Ação futura */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, CorOuroMenuFixo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icone,
                contentDescription = null,
                tint = CorOuroMenuFixo,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = item.titulo, color = CorOuroMenuFixo, fontSize = 14.sp)
        }
    }
}