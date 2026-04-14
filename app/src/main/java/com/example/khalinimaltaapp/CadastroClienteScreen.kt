package com.example.khalinimaltaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khalinimaltaapp.ui.theme.KhaliniMaltaAppTheme
import com.example.khalinimaltaapp.viewmodel.CadastroClienteViewModel

@Composable
fun CadastroClienteScreen(
    onContinuar: () -> Unit,
    // Deixamos apenas o tipo aqui, a MainActivity passará o sharedVM
    viewModel: CadastroClienteViewModel
) {
    val corDourada = Color(0xFFC79E5E)

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
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(195.dp))

            Text(
                text = "Cadastro Cliente",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            CampoExterno("Nome", viewModel.nome, { viewModel.nome = it }, corDourada)
            CampoExterno("Sobrenome", viewModel.sobrenome, { viewModel.sobrenome = it }, corDourada)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) {
                    CampoExterno("Data Nasc.", viewModel.dataNasc, { viewModel.dataNasc = it }, corDourada)
                }
                Box(Modifier.weight(1f)) {
                    CampoExterno("Celular", viewModel.foneCelular, { viewModel.foneCelular = it }, corDourada)
                }
            }

            CampoExterno("CPF", viewModel.cpf, { viewModel.cpf = it }, corDourada)
            CampoExterno("E-mail", viewModel.email, { viewModel.email = it }, corDourada)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Agora apenas navegamos. Os dados ficam guardados no viewModel!
                    onContinuar()
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(46.dp)
                    .offset(y = (-45).dp),
                colors = ButtonDefaults.buttonColors(containerColor = corDourada),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "CONTINUAR PARA SENHA",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun CampoExterno(label: String, value: String, onValueChange: (String) -> Unit, cor: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(text = label, color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(start = 2.dp, bottom = 2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 14.sp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = cor,
                unfocusedBorderColor = cor,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCadastro() {
    KhaliniMaltaAppTheme {
        Surface(color = Color.Black) {
            Text("Visualize no Emulador para testar", color = Color.White)
        }
    }
}