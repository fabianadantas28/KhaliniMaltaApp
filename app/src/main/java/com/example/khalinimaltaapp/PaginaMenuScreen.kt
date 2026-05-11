package com.example.khalinimaltaapp

import androidx.compose.foundation.BorderStroke // Adicione este
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape // Adicione este
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Adicione este
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(
    onNavegar: (String) -> Unit,
    onLogout: () -> Unit // ADICIONE ESTA LINHA AQUI
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
            Spacer(modifier = Modifier.height(130.dp))
            Text("MENU PRINCIPAL", color = CorOuroMenuFixo, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(25.dp))

            Box(modifier = Modifier.weight(1f)) {
                val itensPrincipal = listOf(
                    MenuItemData("Vendas", Icons.Default.ShoppingCart, "vendas"),
                    MenuItemData("Produtos", Icons.Default.List, "pagina_categorias"),
                    MenuItemData("Administração", Icons.Default.Settings, "menu_administracao")
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(itensPrincipal) { item -> MenuCard(item, onNavegar) }
                }
            }

            // --- NOVOS BOTÕES NO RODAPÉ ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onNavegar("home") }, // Ajustado para "home" conforme sua MainActivity
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, CorOuroMenuFixo),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Home, contentDescription = null, tint = CorOuroMenuFixo, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("INÍCIO", color = CorOuroMenuFixo, fontSize = 12.sp)
                }

                Button(
                    onClick = onLogout, // Agora o compilador vai reconhecer este parâmetro
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF440000)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("SAIR", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}