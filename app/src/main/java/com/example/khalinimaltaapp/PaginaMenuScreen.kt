package com.example.khalinimaltaapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.viewmodel.CadastroClienteViewModel

val CorOuroMenuPrincipalUnico = Color(0xFFC79E5E)

data class MenuPrincipalItemData(
    val titulo: String,
    val icone: ImageVector,
    val rota: String
)

@Composable
fun MenuScreen(
    sharedViewModel: CadastroClienteViewModel, // Recebe o ViewModel injetado da MainActivity
    onNavegar: (String) -> Unit,
    onLogout: () -> Unit
) {
    // Estado para controlar se mostra a tela de seleção de comprador
    var exibirSelecaoComprador by remember { mutableStateOf(false) }

    if (exibirSelecaoComprador) {
        // Exibe a tela de seleção por cima do menu
        SelecaoCompradorScreen(
            onSelecionar = { perfil ->
                // Altera o nome diretamente no motor reativo global do app
                sharedViewModel.nome = perfil
                exibirSelecaoComprador = false
                // Navega para a vitrine de produtos
                onNavegar("pagina_categorias")
            },
            onCancelar = {
                exibirSelecaoComprador = false
            }
        )
    } else {
        // Exibe o Menu Principal normalmente
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
                Text(
                    text = "MENU PRINCIPAL",
                    color = CorOuroMenuPrincipalUnico,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(25.dp))

                Box(modifier = Modifier.weight(1f)) {
                    val itensPrincipal = listOf(
                        MenuPrincipalItemData("Venda Balcão", Icons.Default.ShoppingCart, "venda_balcao"),
                        MenuPrincipalItemData("Produtos", Icons.Default.List, "pagina_categorias"),
                        MenuPrincipalItemData("Administração", Icons.Default.Settings, "menu_administracao")
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(itensPrincipal) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clickable {
                                        if (item.rota == "venda_balcao") {
                                            exibirSelecaoComprador = true
                                        } else {
                                            onNavegar(item.rota)
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF121A24).copy(alpha = 0.85f)),
                                border = BorderStroke(1.dp, CorOuroMenuPrincipalUnico.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = item.icone,
                                        contentDescription = item.titulo,
                                        tint = CorOuroMenuPrincipalUnico,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = item.titulo,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onNavegar("home") },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, CorOuroMenuPrincipalUnico),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = CorOuroMenuPrincipalUnico, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("INÍCIO", color = CorOuroMenuPrincipalUnico, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onLogout,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF440000)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SAIR", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}