package com.example.khalinimaltaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroVendaScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Barra Superior
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = CorOuroKhalini)
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = CorOuroKhalini)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Registro de Venda", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // Buscar Produtos
        Text("Buscar Produtos", color = CorOuroKhalini, fontSize = 14.sp)
        CampoRegistro(hint = "Digite o nome ou código do produto...", icon = Icons.Default.Search)

        Spacer(modifier = Modifier.height(20.dp))

        // Informações do Cliente
        Text("Informações do Cliente", color = CorOuroKhalini, fontSize = 14.sp)
        CampoRegistro(hint = "Nome do cliente (opcional)", icon = Icons.Default.Face) // Ícone universal
        Spacer(modifier = Modifier.height(8.dp))
        CampoRegistro(hint = "Telefone do cliente (para WhatsApp)", icon = null)

        Spacer(modifier = Modifier.height(20.dp))

        // Forma de Pagamento
        Text("Forma de Pagamento", color = CorOuroKhalini, fontSize = 14.sp)
        // Usei o ícone 'Star' que é garantido que não dará erro de build
        CampoRegistro(hint = "Selecione a forma de pagamento", icon = Icons.Default.Star, isDropdown = true)

        Spacer(modifier = Modifier.weight(1f))

        // Botão Confirmar
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            border = androidx.compose.foundation.BorderStroke(1.dp, CorOuroKhalini),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Done, contentDescription = null, tint = CorOuroKhalini)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Confirmar Venda", color = CorOuroKhalini)
        }

        // Botões Enviar e Cancelar
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorOuroKhalini),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Email, contentDescription = null, tint = CorOuroKhalini)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Enviar WhatsApp", color = CorOuroKhalini, fontSize = 12.sp)
            }

            Button(
                onClick = { },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Clear, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Cancelar", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoRegistro(hint: String, icon: androidx.compose.ui.graphics.vector.ImageVector?, isDropdown: Boolean = false) {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text(hint, color = Color.Gray, fontSize = 14.sp) },
        leadingIcon = if (icon != null) { { Icon(icon, contentDescription = null, tint = CorOuroKhalini) } } else null,
        trailingIcon = if (isDropdown) { { Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray) } } else null,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1A1A1A),
            unfocusedContainerColor = Color(0xFF1A1A1A),
            focusedBorderColor = CorOuroKhalini,
            unfocusedBorderColor = Color.DarkGray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegistroVenda() {
    RegistroVendaScreen()
}