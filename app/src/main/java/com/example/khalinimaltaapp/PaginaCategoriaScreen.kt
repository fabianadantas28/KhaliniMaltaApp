package com.example.khalinimaltaapp

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Mantendo suas cores originais
val CorOuroCaixinha = Color(0xFFC39953)
val CorOuroBorda = Color(0xFFC79E5E)

data class CategoriaItem(
    val nome: String,
    val imagemRes: Int,
    val rotaBase: String // Rota base para a lista
)

@Composable
fun PaginaCategorias(
    navController: NavController,
    onIrParaCadastroProduto: () -> Unit // Nova função para o botão
) {
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
            Spacer(modifier = Modifier.height(230.dp))

            Text(
                text = "CATEGORIAS",
                color = CorOuroBorda,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // GRID DE CATEGORIAS
            Box(modifier = Modifier.weight(1f)) {
                CategoriaGrid(navController)
            }

            // --- NOVO BOTÃO: CADASTRAR PRODUTO ---
            Button(
                onClick = onIrParaCadastroProduto,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(45.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CorOuroBorda)
            ) {
                Text(
                    "CADASTRAR NOVO PRODUTO",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // BOTÃO VOLTAR
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(42.dp),
                border = BorderStroke(1.dp, CorOuroBorda),
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

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun CategoriaGrid(navController: NavController) {
    // Definindo as categorias que levarão para a lista_produtos/{nome}
    val categorias = listOf(
        CategoriaItem("Anéis", R.drawable.anel, "lista_produtos/Anéis"),
        CategoriaItem("Colares", R.drawable.colar, "lista_produtos/Colares"),
        CategoriaItem("Brincos", R.drawable.brinco, "lista_produtos/Brincos"),
        CategoriaItem("Pulseiras", R.drawable.pulseira, "lista_produtos/Pulseiras"),
        CategoriaItem("Tornozeleiras", R.drawable.tornozeleira, "lista_produtos/Tornozeleiras"),
        CategoriaItem("Acessórios", R.drawable.acessorios, "lista_produtos/Acessórios")
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
            .aspectRatio(1.3f)
            .clickable { navController.navigate(item.rotaBase) },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(0.5.dp, CorOuroBorda)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CorOuroCaixinha),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = item.imagemRes),
                    contentDescription = item.nome,
                    modifier = Modifier.size(38.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
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