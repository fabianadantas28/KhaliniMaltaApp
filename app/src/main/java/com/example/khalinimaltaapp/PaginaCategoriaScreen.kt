package com.example.khalinimaltaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// TOM DE OURO DA FIGURA ENVIADA (Dourado Terroso)
val CorOuroCaixinha = Color(0xFFC39953)
val CorOuroBorda = Color(0xFFC79E5E)

data class CategoriaItem(
    val nome: String,
    val imagemRes: Int,
    val rota: String
)

@Composable
fun PaginaCategorias(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {

        // FUNDO
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
            // DESCER O NOME CATEGORIAS
            Spacer(modifier = Modifier.height(250.dp))

            Text(
                text = "CATEGORIAS",
                color = CorOuroBorda,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // GRID
            Box(modifier = Modifier.weight(1f)) {
                CategoriaGrid(navController)
            }

            // SUBIR O BOTÃO VOLTAR
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(42.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorOuroBorda),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.8f)
                )
            ) {
                Text(
                    "VOLTAR AO MENU",
                    color = CorOuroBorda,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Margem inferior para o botão ficar na posição correta
            Spacer(modifier = Modifier.height(105.dp))
        }
    }
}

@Composable
fun CategoriaGrid(navController: NavController) {
    val categorias = listOf(
        CategoriaItem("Anéis", R.drawable.anel, "aneis"),
        CategoriaItem("Colares", R.drawable.colar, "colares"),
        CategoriaItem("Brincos", R.drawable.brinco, "brincos"),
        CategoriaItem("Pulseiras", R.drawable.pulseira, "pulseiras"),
        CategoriaItem("Tornozeleiras", R.drawable.tornozeleira, "tornozeleiras"),
        CategoriaItem("Acessórios", R.drawable.acessorios, "acessorios")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
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
            .fillMaxWidth()
            .aspectRatio(1.3f) // Caixas menores e mais baixas
            .clickable { navController.navigate(item.rota) },
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, CorOuroBorda)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CorOuroCaixinha), // COR EXATA DA SUA IMAGEM
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = item.imagemRes),
                    contentDescription = item.nome,
                    modifier = Modifier.size(38.dp),
                    colorFilter = ColorFilter.tint(Color.Black) // Ícones em preto para contraste
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.nome,
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPaginaCategorias() {
    PaginaCategorias(navController = rememberNavController())
}