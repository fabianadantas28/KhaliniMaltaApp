package com.example.khalinimaltaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.viewmodel.CadastroUsuarioViewModel

@Composable
fun CadastroUsuarioScreen(
    onVoltar: () -> Unit,
    viewModel: CadastroUsuarioViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val dourado = Color(0xFFC79E5E)
    val scrollState = rememberScrollState()

    // Navega de volta após salvar com sucesso
    LaunchedEffect(viewModel.salvouComSucesso) {
        if (viewModel.salvouComSucesso) {
            viewModel.limparCampos()
            onVoltar()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo_preto),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Cadastro de Funcionário",
                color = dourado,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Nome
            OutlinedTextField(
                value = viewModel.nome,
                onValueChange = { viewModel.nome = it },
                label = { Text("Nome completo", color = dourado) },
                modifier = Modifier.fillMaxWidth(),
                colors = campoDourado(dourado),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("E-mail", color = dourado) },
                modifier = Modifier.fillMaxWidth(),
                colors = campoDourado(dourado),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Senha temporária
            OutlinedTextField(
                value = viewModel.senhaTemporaria,
                onValueChange = { viewModel.senhaTemporaria = it },
                label = { Text("Senha temporária", color = dourado) },
                modifier = Modifier.fillMaxWidth(),
                colors = campoDourado(dourado),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Seletor de Perfil
            Text("Perfil de acesso", color = dourado, fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Start))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("FUNCIONARIO", "ADMIN").forEach { opcao ->
                    val selecionado = viewModel.perfil == opcao
                    OutlinedButton(
                        onClick = { viewModel.perfil = opcao },
                        modifier = Modifier.weight(1f),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (selecionado) 2.dp else 1.dp,
                            color = if (selecionado) dourado else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selecionado) dourado.copy(alpha = 0.15f)
                            else Color.Transparent,
                            contentColor = if (selecionado) dourado else Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(opcao, fontWeight = if (selecionado) FontWeight.Bold
                        else FontWeight.Normal)
                    }
                }
            }

            // Mensagem de erro
            if (viewModel.mensagemErro.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = viewModel.mensagemErro,
                    color = Color.Red,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botão Salvar
            Button(
                onClick = { viewModel.salvar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = dourado,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cadastrar Funcionário", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão Voltar
            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, dourado),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = dourado)
            ) {
                Text("Cancelar", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun campoDourado(dourado: Color) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = dourado,
    unfocusedBorderColor = dourado,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = dourado,
    unfocusedLabelColor = dourado
)
