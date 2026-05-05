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
                    // AJUSTADO: Agora aponta para lista_cliente em vez de cadastro
                    MenuItemData("Clientes", Icons.Default.Person, "lista_cliente"),
                    MenuItemData("Cadastrar Produto", Icons.Default.Add, "cadastro_produto"),
                    MenuItemData("Estoque", Icons.Default.Build, "controle_estoque"),
                    MenuItemData("Relatórios", Icons.Default.Info, "relatorios"),
                    MenuItemData("Usuários", Icons.Default.AccountBox, "cadastro_usuario")
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
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
        modifier = Modifier.aspectRatio(1f).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.titulo, color = CorOuroMenuFixo, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}