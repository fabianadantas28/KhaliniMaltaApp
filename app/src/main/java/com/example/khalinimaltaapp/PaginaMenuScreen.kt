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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// COR PADRÃO KHALINI MALTA
val CorOuroKhaliniMenu = Color(0xFFC79E5E)

// MODELO DO ITEM
data class MenuItem(
    val titulo: String,
    val icone: ImageVector,
    val rota: String
)

@Composable
fun PaginaMenu(navController: NavController) {
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
                .padding(horizontal = 40.dp), // Aperta mais os lados para os botões encolherem
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. ESPAÇO PARA A LOGOMARCA
            // Aumentamos para 200dp para o texto "Menu Principal" brotar LOGO ABAIXO do círculo central
            Spacer(modifier = Modifier.height(210.dp))

            Text(
                text = "MENU PRINCIPAL",
                color = CorOuroKhaliniMenu,
                fontSize = 20.sp, // Diminuí um pouco a fonte para ficar mais elegante
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            // 2. GRID DE OPÇÕES (Botões menores)
            // Aumentei o padding horizontal aqui para os botões ficarem mais estreitos e delicados
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                MenuGrid(navController)
            }

            // 3. BOTÃO VOLTAR (Subindo para dentro do quadrado)
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Botão mais curto
                    .height(45.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorOuroKhaliniMenu),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.8f)
                )
            ) {
                Text(
                    text = "VOLTAR AO INÍCIO",
                    color = CorOuroKhaliniMenu,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // 4. MARGEM DE SEGURANÇA INFERIOR
            // Aumentei para 110dp para garantir que o botão suba e saia da borda dourada de baixo
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}
@Composable
fun MenuGrid(navController: NavController) {
    val itens = listOf(
        MenuItem("Clientes", Icons.Default.Person, "clientes"),
        MenuItem("Produtos", Icons.Default.ShoppingCart, "produtos"),
        MenuItem("Vendas", Icons.Default.ThumbUp, "vendas"),
        MenuItem("Relatórios", Icons.Default.Info, "relatorios"),
        MenuItem("Estoque", Icons.Default.Build, "estoque")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(itens) { item ->
            MenuCard(item, navController)
        }
    }
}

@Composable
fun MenuCard(item: MenuItem, navController: NavController) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { navController.navigate(item.rota) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, CorOuroKhaliniMenu)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icone,
                contentDescription = null,
                tint = CorOuroKhaliniMenu,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.titulo,
                color = CorOuroKhaliniMenu,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// PREVIEW COMPLETO
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPaginaMenu() {
    val navController = rememberNavController()
    PaginaMenu(navController = navController)
}