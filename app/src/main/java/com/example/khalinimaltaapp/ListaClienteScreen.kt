package com.example.khalinimaltaapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.data.Cliente
import com.example.khalinimaltaapp.viewmodel.ListaClientesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaClientesScreen(
    viewModel: ListaClientesViewModel,
    onVoltar: () -> Unit,
    onIrParaCadastro: () -> Unit
) {
    val corDourada = Color(0xFFC79E5E)

    // Observamos tanto a lista quanto o texto da busca
    val listaClientesDb by viewModel.clientes.collectAsState()
    val textoBusca by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MEUS CLIENTES", color = corDourada, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = corDourada)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onIrParaCadastro, containerColor = corDourada) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            // --- BARRA DE PESQUISA ---
            OutlinedTextField(
                value = textoBusca,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Procurar cliente...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = corDourada) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = corDourada,
                    unfocusedBorderColor = Color(0xFF333333),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1A1A1A),
                    unfocusedContainerColor = Color(0xFF1A1A1A)
                )
            )

            // --- EXIBIÇÃO DA LISTA ---
            Box(modifier = Modifier.fillMaxSize()) {
                if (listaClientesDb.isEmpty()) {
                    // BOTÃO DE VOLTAR QUANDO A LISTA ESTÁ VAZIA
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (textoBusca.isEmpty()) "Nenhum cliente cadastrado." else "Nenhum resultado.",
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = onVoltar,
                            border = BorderStroke(1.dp, corDourada)
                        ) {
                            Text("VOLTAR AO MENU", color = corDourada)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(listaClientesDb) { cliente ->
                            CardCliente(cliente, corDourada)
                        }

                        // ITEM DE BOTÃO AO FINAL DA LISTA
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            OutlinedButton(
                                onClick = onVoltar,
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, corDourada),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("VOLTAR AO MENU", color = corDourada, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardCliente(cliente: Cliente, corDourada: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = corDourada,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${cliente.nome} ${cliente.sobrenome}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Tel: ${cliente.telefone}",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Text(
                    text = cliente.email,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}
