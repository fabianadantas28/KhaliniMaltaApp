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

val CorOuroBorda = Color(0xFFC79E5E)
val CorOuroCaixinha = Color(0xFFC39953)

data class CategoriaItem(val nome: String, val imagemRes: Int)

@Composable
fun PaginaCategoriaScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo_logomarca),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(150.dp))
            Text("CATEGORIAS", color = CorOuroBorda, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.weight(1f)) {
                val categorias = listOf(
                    CategoriaItem("Anéis", R.drawable.anel),
                    CategoriaItem("Colares", R.drawable.colar),
                    CategoriaItem("Brincos", R.drawable.brinco),
                    CategoriaItem("Pulseiras", R.drawable.pulseira),
                    CategoriaItem("Tornozeleiras", R.drawable.tornozeleira),
                    CategoriaItem("Acessórios", R.drawable.acessorios)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categorias) { categoria ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.3f)
                                .clickable {
                                    navController.navigate("lista_produtos/${categoria.nome}")
                                },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(0.5.dp, CorOuroBorda)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().background(CorOuroCaixinha), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Image(
                                        painter = painterResource(id = categoria.imagemRes),
                                        contentDescription = null,
                                        modifier = Modifier.size(38.dp),
                                        colorFilter = ColorFilter.tint(Color.Black)
                                    )
                                    Text(categoria.nome, color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // BOTÃO ATUALIZADO: De "VOLTAR" para "FINALIZAR"
            OutlinedButton(
                onClick = {
                    // Navega para o login e limpa todo o histórico de telas
                    navController.navigate("login") {
                        popUpTo(0) // Remove todas as telas anteriores da pilha
                    }
                },
                modifier = Modifier.fillMaxWidth(0.85f).height(42.dp),
                border = BorderStroke(1.dp, CorOuroBorda),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Black.copy(alpha = 0.8f))
            ) {
                Text("FINALIZAR", color = CorOuroBorda, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}