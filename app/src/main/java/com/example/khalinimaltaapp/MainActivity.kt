package com.example.khalinimaltaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.khalinimaltaapp.ui.theme.KhaliniMaltaAppTheme
import com.example.khalinimaltaapp.viewmodel.CadastroClienteViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.khalinimaltaapp.data.database.AppDatabase
import com.example.khalinimaltaapp.viewmodel.ListaClientesViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhaliniMaltaAppTheme {
                val navController = rememberNavController()

                // Criamos o ViewModel UM ÚNICA VEZ aqui fora para ser compartilhado
                // Ele agora guarda tanto os dados do cliente quanto a senha
                val sharedViewModel: CadastroClienteViewModel = viewModel()

                NavHost(navController = navController, startDestination = "login") {

                    // 1. Rota de Login
                    composable(route = "login") {
                        LoginScreen(
                            onIrParaPaginaInicial = { navController.navigate("home") },
                            onIrParaCadastro = { navController.navigate("cadastro_cliente") }
                        )
                    }

                    // 2. Rota de Cadastro
                    composable(route = "cadastro_cliente") {
                        CadastroClienteScreen(
                            onContinuar = { navController.navigate("criar_senha") },
                            viewModel = sharedViewModel // Usa o motor compartilhado
                        )
                    }

                    // 3. Rota de Senha
                    composable(route = "criar_senha") {
                        CriarSenhaScreen(
                            onFinalizar = {
                                // Navega de volta para o login e limpa o histórico
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            viewModel = sharedViewModel // Usa o MESMO motor da tela anterior
                        )
                    }

                    // 4. Outras Rotas
                    composable(route = "home") {
                        PaginaPrincipalKM(
                            onAbrirMenu = { navController.navigate("menu") },
                            onIrParaLogin = { navController.navigate("login") }
                        )
                    }

                    composable("menu") {
                        MenuScreen(
                            onVoltar = { navController.popBackStack() },
                            onNavegar = { rota ->
                                // Garante que se o menu pedir "produtos", ele vá para as Categorias
                                val destino = if (rota == "produtos") "categorias" else rota
                                navController.navigate(destino)
                            }
                        )
                    }

                    // 5. Categorias (Onde você escolhe o tipo de joia)
                    composable("categorias") {
                        PaginaCategorias(
                            navController = navController,
                            onIrParaCadastroProduto = { navController.navigate("cadastro_produto") }
                        )
                    }

                    // 6. Cadastro de Produto (A tela que criamos hoje)
                    composable("cadastro_produto") {
                        val db = AppDatabase.getDatabase(LocalContext.current)
                        CadastroProdutoScreen(
                            navController = navController,
                            produtoDao = db.produtoDao()
                        )
                    }

                    // 7. Lista de Produtos (Filtrada por categoria)
                    composable("lista_produtos/{nomeCategoria}") { backStackEntry ->
                        val categoriaDigitada = backStackEntry.arguments?.getString("nomeCategoria") ?: "Produtos"
                        ListaProdutosScreen(
                            categoria = categoriaDigitada,
                            onVoltar = { navController.popBackStack() },
                            onIrParaCadastro = { navController.navigate("cadastro_produto") }
                        )
                    }

                    // 8. Lista de Clientes
                    composable(route = "lista_clientes") {
                        val context = LocalContext.current
                        val listaViewModel: ListaClientesViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    val database = AppDatabase.getDatabase(context)
                                    return ListaClientesViewModel(database.clienteDao()) as T
                                }
                            }
                        )
                        ListaClientesScreen(
                            viewModel = listaViewModel,
                            onVoltar = { navController.popBackStack() },
                            onIrParaCadastro = { navController.navigate("cadastro_cliente") }
                        )
                    }
                } // Fim do NavHost
            } // Fim do Theme
        } // Fim do setContent
    } // Fim do onCreate
} // Fim da MainActivity