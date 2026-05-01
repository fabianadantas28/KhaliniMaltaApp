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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
// IMPORTANTE: Importando o ViewModel que agora faz tudo
import com.example.khalinimaltaapp.viewmodel.CadastroClienteViewModel
import com.example.khalinimaltaapp.ui.theme.KhaliniMaltaAppTheme

@Composable
fun CriarSenhaScreen(
    onFinalizar: () -> Unit,
    viewModel: CadastroClienteViewModel // Agora usando o motor compartilhado!
) {
    val dourado = Color(0xFFD4AF37)
    val scrollState = rememberScrollState()

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
                text = "Cadastro Senha",
                color = dourado,
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            CustomTextField(
                valor = viewModel.senha,
                aoMudar = { viewModel.senha = it },
                label = "Digite sua Senha",
                corDourada = dourado,
                isSenha = true
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                valor = viewModel.confirmarSenha,
                aoMudar = { viewModel.confirmarSenha = it },
                label = "Confirmar Senha",
                corDourada = dourado,
                isSenha = true
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    // 1. Verificamos se as senhas batem antes de tentar salvar
                    if (viewModel.senha.isNotEmpty() && viewModel.senha == viewModel.confirmarSenha) {

                        // 2. Chama a função que grava no SQLite
                        viewModel.salvarNoBanco()

                        // 3. Navega para o Login
                        onFinalizar()

                    } else {
                        // Caso as senhas estejam diferentes ou vazias
                        // Log.w ou um Toast ajudaria aqui
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC79E5E),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Finalizar Cadastro",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CustomTextField(
    valor: String,
    aoMudar: (String) -> Unit,
    label: String,
    corDourada: Color,
    isSenha: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        label = { Text(label, color = corDourada) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White),
        visualTransformation = if (isSenha) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = corDourada,
            unfocusedBorderColor = corDourada,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSenha() {
    KhaliniMaltaAppTheme {
        Surface(color = Color.Black) {
            Text("Visualize no celular para testar", color = Color.White)
        }
    }
}