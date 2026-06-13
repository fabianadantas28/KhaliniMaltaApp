package com.example.khalinimaltaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.viewmodel.ListaFuncionariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaFuncionariosScreen(
    onVoltar: () -> Unit,
    onIrParaCadastro: () -> Unit, // <--- Nova ação adicionada
    viewModel: ListaFuncionariosViewModel
) {
    val listaFuncionarios by viewModel.funcionarios.collectAsState()
    val corOuro = Color(0xFFC39953)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FUNCIONÁRIOS", color = corOuro, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = corOuro)
                    }
                },
                actions = {
                    // BOTÃO DE CADASTRO ADICIONADO NO TOPO DIREITO
                    IconButton(onClick = onIrParaCadastro) {
                        Icon(Icons.Default.Add, contentDescription = "Cadastrar Funcionário", tint = corOuro)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (listaFuncionarios.isEmpty()) {
                Text(
                    text = "Nenhum funcionário cadastrado.",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listaFuncionarios) { funcionario ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = funcionario.nome.uppercase(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "E-mail: ${funcionario.email}",
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Acesso: ${funcionario.perfil}",
                                        color = corOuro,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.excluirFuncionario(funcionario.id) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Excluir Funcionário",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}