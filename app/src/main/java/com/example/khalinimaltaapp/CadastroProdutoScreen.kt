package com.example.khalinimaltaapp

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.khalinimaltaapp.viewmodel.CadastroProdutoViewModel

val CorOuroEnvelhecido = Color(0xFFC79E5E)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoProduto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(text = label, color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(bottom = 2.dp, start = 4.dp))
        OutlinedTextField(
            value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp), keyboardOptions = keyboardOptions, readOnly = readOnly,
            trailingIcon = trailingIcon, textStyle = TextStyle(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = CorOuroEnvelhecido, unfocusedBorderColor = CorOuroEnvelhecido.copy(alpha = 0.7f),
                focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun CadastroProdutoScreen(navController: NavController, viewModel: CadastroProdutoViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val gap = 4.dp

    // --- LÓGICA DE IMAGEM ATIVA ---
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    LaunchedEffect(imageUri) {
        imageUri?.let {
            try {
                bitmap = if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        // Força o Android a processar via Software, evitando quebra em aparelhos novos
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                        decoder.isMutableRequired = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    // ------------------------------

    val categoriasOficiais = listOf("Anéis", "Colares", "Brincos", "Pulseiras", "Tornozeleiras", "Acessórios")
    var menuExpandido by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo_logomarca),
            contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp).verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.size(48.dp)) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar", tint = CorOuroEnvelhecido, modifier = Modifier.size(32.dp))
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(text = "Cadastro Produto", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)

            // QUADRADO DA FOTO (AGORA CLICÁVEL E ATIVO)
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(85.dp)
                        .border(1.5.dp, CorOuroEnvelhecido, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { launcher.launch("image/*") }, // ABRE A GALERIA
                    contentAlignment = Alignment.Center
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AddAPhoto, null, tint = CorOuroEnvelhecido)
                            Text("Foto", color = CorOuroEnvelhecido, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(gap))

            CampoProduto("Nome Produto", viewModel.nome, { viewModel.nome = it })
            Spacer(modifier = Modifier.height(gap))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoProduto("Marca", viewModel.marca, { viewModel.marca = it }, Modifier.weight(1f))
                CampoProduto("Material", viewModel.material, { viewModel.material = it }, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(gap))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoProduto("Código Interno", viewModel.codigo, { viewModel.codigo = it }, Modifier.weight(1f))
                CampoProduto(label = "Preço (R$)", value = viewModel.preco, onValueChange = { viewModel.preco = it },
                    modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
            Spacer(modifier = Modifier.height(gap))

            CampoProduto("Descrição", viewModel.descricao, { viewModel.descricao = it }, Modifier.height(90.dp))
            Spacer(modifier = Modifier.height(gap))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Bottom) {
                Box(modifier = Modifier.weight(1.5f)) {
                    CampoProduto(label = "Categoria", value = viewModel.categoria, onValueChange = {}, readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { menuExpandido = true }) {
                                Icon(imageVector = Icons.Default.AddAPhoto, null, tint = CorOuroEnvelhecido)
                            }
                        }
                    )
                    DropdownMenu(expanded = menuExpandido, onDismissRequest = { menuExpandido = false }, modifier = Modifier.fillMaxWidth(0.5f)) {
                        categoriasOficiais.forEach { nomeCat ->
                            DropdownMenuItem(text = { Text(nomeCat) }, onClick = { viewModel.categoria = nomeCat; menuExpandido = false })
                        }
                    }
                }
                CampoProduto(label = "Qtde", value = viewModel.estoque, onValueChange = { viewModel.estoque = it },
                    modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.salvarProduto(
                        onSucesso = {
                            Toast.makeText(context, "Produto salvo!", Toast.LENGTH_SHORT).show()
                            navController.navigate("controle_estoque") { popUpTo("cadastro_produto") { inclusive = true } }
                        },
                        onError = { erro -> Toast.makeText(context, erro, Toast.LENGTH_LONG).show() }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = CorOuroEnvelhecido),
                shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Cadastrar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}