package com.example.khalinimaltaapp.ui.categoria

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.khalinimaltaapp.R // Importante para puxar o fundo

// COR PADRÃO KHALINI MALTA
val CorOuroCategorias = Color(0xFFC79E5E)

data class CategoriaItem(
    val nome: String,
    val icone: ImageVector, // Mudamos para ícone real
    val rota: String
)

@Composable
fun PaginaCategorias(navController: NavController) {
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
                .padding(horizontal = 55.dp), // Aumentei para 55dp para os botões ficarem bem pequenos e centralizados
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. ESPAÇO PARA A LOGO (Título desce para baixo do círculo)
            Spacer(modifier = Modifier.height(215.dp))

            Text(
                text = "CATEGORIAS",
                color = CorOuroCategorias,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. GRID DE CATEGORIAS (Botões mais compactos)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp) // Respiro extra interno
            ) {
                CategoriaGrid(navController)
            }

            // 3. BOTÃO VOLTAR (Subindo para o meio do quadrado inferior)
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(45.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorOuroCategorias),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.8f)
                )
            ) {
                Text(
                    text = "VOLTAR AO MENU",
                    color = CorOuroCategorias,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // 4. MARGEM DE FUNDO (Aumentada para o botão subir mais)
            Spacer(modifier = Modifier.height(130.dp))
        }
    }
}

@Composable
fun CategoriaGrid(navController: NavController) {
    // Definindo as joias com ícones que fazem sentido
    val categorias = listOf(
        CategoriaItem("Anéis", Icons.Default.Favorite, "aneis"),
        CategoriaItem("Colares", Icons.Default.Star, "colares"),
        CategoriaItem("Brincos", Icons.Default.Face, "brincos"),
        CategoriaItem("Pulseiras", Icons.Default.Refresh, "pulseiras"), // Corrigi a aspa que o João esqueceu
        CategoriaItem("Tornozeleiras", Icons.Default.LocationOn, "tornozeleiras"),
        CategoriaItem("Acessórios", Icons.Default.Add, "acessorios")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categorias) { categoria ->
            CategoriaCard(categoria, navController)
        }
    }
}

@Composable
fun CategoriaCard(item: CategoriaItem, navController: NavController) {
    Card(
        modifier = Modifier
            .aspectRatio(1.1f) // Ligeiramente mais retangular para caber melhor
            .clickable { navController.navigate(item.rota) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, CorOuroCategorias)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icone,
                contentDescription = null,
                tint = CorOuroCategorias,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.nome,
                color = CorOuroCategorias,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// PREVIEW PARA VOCÊ ENXERGAR O ENCAIXE
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPaginaCategorias() {
    val navController = rememberNavController()
    PaginaCategorias(navController = navController)
}