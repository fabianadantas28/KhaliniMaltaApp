package com.example.khalinimaltaapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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

// Definições globais
val CorOuroMenuFixo = Color(0xFFD4AF37)

data class MenuItemData(
    val titulo: String,
    val icone: ImageVector,
    val rota: String
)

@Composable
fun MenuAdministracaoScreen(
    onVoltar: () -> Unit,
    onNavegar: (String) -> Unit
) {
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
            // MEDIDA PARA FICAR ABAIXO DA LOGOMARCA
            Spacer(modifier = Modifier.height(130.dp))

            Text(
                text = "GESTÃO E CONTROLE",
                color = CorOuroMenuFixo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            Box(modifier = Modifier.weight(1f)) {
                val itensAdmin = listOf(
                    MenuItemData("Clientes", Icons.Default.Person, "lista_cliente"),
                    MenuItemData("Cadastrar Produto", Icons.Default.Add, "cadastro_produto"),
                    MenuItemData("Vendas", Icons.Default.ShoppingCart, "gestao_vendas"),
                    MenuItemData("Estoque Atual", Icons.Default.Build, "controle_estoque"),
                    MenuItemData("Repor Estoque", Icons.Default.Refresh, "entrada_estoque"),
                    MenuItemData("Relatórios", Icons.Default.Info, "relatorios"),
                    MenuItemData("Usuários", Icons.Default.AccountBox, "cadastro_usuario")
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp), // Aumentei aqui para as caixas "encolherem" para o centro
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(itensAdmin) { item ->
                        MenuCard(item, onNavegar)
                    }
                }
            }

            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier.fillMaxWidth(0.8f).height(45.dp),
                border = BorderStroke(1.dp, CorOuroMenuFixo),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("VOLTAR AO MENU", color = CorOuroMenuFixo, fontWeight = FontWeight.Bold)
            }
            // ESPAÇO INFERIOR PARA NÃO FICAR COLADO NA BORDA
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuCard(item: MenuItemData, onNavegar: (String) -> Unit) {
    Card(
        onClick = { onNavegar(item.rota) },
        modifier = Modifier.aspectRatio(1.2f).fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, CorOuroMenuFixo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = item.icone,
                contentDescription = item.titulo,
                tint = CorOuroMenuFixo,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.titulo, color = CorOuroMenuFixo, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}