package com.example.khalinimaltaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // ADICIONADO
import androidx.compose.runtime.getValue       // ADICIONADO
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
import androidx.lifecycle.viewmodel.compose.viewModel // ADICIONADO
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.khalinimaltaapp.viewmodel.CategoriaViewModel // ADICIONADO

// SEU DESIGN ORIGINAL (MANTIDO)
val CorOuroCaixinha = Color(0xFFC39953)
val CorOuroBorda = Color(0xFFC79E5E)

@Composable
fun PaginaCategorias(
    navController: NavController,
    viewModel: CategoriaViewModel = viewModel() // 1. CONECTADO O VIEWMODEL
) {
    // 2. PEGANDO A LISTA DO VIEWMODEL
    val categorias by viewModel.categorias.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        // SEU FUNDO ORIGINAL
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
            Spacer(modifier = Modifier.height(250.dp))

            Text(
                text = "CATEGORIAS",
                color = CorOuroBorda,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // GRID USANDO A LISTA DO VIEWMODEL
            Box(modifier = Modifier.weight(1f)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 3. AGORA USA OS ITENS QUE VEM DO VIEWMODEL
                    items(categorias) { categoria ->
                        CategoriaCard(categoria, navController)
                    }
                }
            }

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

            Spacer(modifier = Modifier.height(135.dp))
        }
    }
}

@Composable
fun CategoriaCard(item: com.example.khalinimaltaapp.viewmodel.CategoriaItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.3f)
            .clickable { navController.navigate(item.rota) },
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, CorOuroBorda)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPaginaCategorias() {
    PaginaCategorias(navController = rememberNavController())
}