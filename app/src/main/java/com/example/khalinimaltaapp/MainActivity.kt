package com.example.khalinimaltaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.khalinimaltaapp.data.database.AppDatabase
import com.example.khalinimaltaapp.ui.theme.KhaliniMaltaAppTheme
import com.example.khalinimaltaapp.viewmodel.*
import com.example.khalinimaltaapp.ui.relatorio.PaginaRelatorio
import com.example.khalinimaltaapp.ui.estoque.PaginaControleEstoque




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhaliniMaltaAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val db = AppDatabase.getDatabase(context)

                val sharedViewModel: CadastroClienteViewModel = viewModel()

                val vendaViewModel: RegistroVendaViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return RegistroVendaViewModel(db.produtoDao(), db.vendaDao()) as T
                        }
                    }
                )

                NavHost(navController = navController, startDestination = "splash") {

                    // 0. SPLASH SCREEN
                    composable(route = "splash") {
                        SplashScreen(
                            onTimeout = {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 1. LOGIN
                    composable(route = "login") {
                        LoginScreen(
                            navController = navController,
                            onIrParaPaginaInicial = { navController.navigate("home") },
                            onIrParaCadastro = { navController.navigate("cadastro_cliente") }
                        )
                    }

                    // 2. TROCA DE SENHA
                    composable("troca_senha/{usuarioId}") { backStackEntry ->
                        val usuarioId = backStackEntry.arguments?.getString("usuarioId")?.toInt() ?: 0
                        TrocaSenhaScreen(
                            usuarioId = usuarioId,
                            onSenhaAtualizada = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("cadastro_usuario") {
                        CadastroUsuarioScreen(onVoltar = { navController.popBackStack() })
                    }

                    composable(route = "cadastro_cliente") {
                        CadastroClienteScreen(
                            onContinuar = { navController.navigate("criar_senha") },
                            viewModel = sharedViewModel
                        )
                    }

                    composable(route = "criar_senha") {
                        CriarSenhaScreen(
                            onFinalizar = {
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            viewModel = sharedViewModel
                        )
                    }

                    composable(route = "home") {
                        PaginaPrincipalKM(
                            onAbrirMenu = { navController.navigate("menu") },
                            onIrParaLogin = {
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("menu") {
                        MenuScreen(onNavegar = { rota -> navController.navigate(rota) })
                    }

                    composable(route = "pagina_categorias") {
                        PaginaCategoriaScreen(navController = navController)
                    }

                    composable("menu_administracao") {
                        MenuAdministracaoScreen(
                            onVoltar = { navController.popBackStack() },
                            onNavegar = { rota -> navController.navigate(rota) }
                        )
                    }

                    composable("cadastro_produto") {
                        val vModel: CadastroProdutoViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return CadastroProdutoViewModel(db.produtoDao()) as T
                                }
                            }
                        )
                        CadastroProdutoScreen(navController = navController, viewModel = vModel)
                    }

                    composable("lista_produtos/{categoriaNome}") { backStackEntry ->
                        val categoria = backStackEntry.arguments?.getString("categoriaNome") ?: ""
                        val vModel: CadastroProdutoViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return CadastroProdutoViewModel(db.produtoDao()) as T
                                }
                            }
                        )
                        ListaProdutosScreen(
                            navController = navController,
                            categoriaSelecionada = categoria,
                            viewModel = vModel
                        )
                    }

                    composable("controle_estoque") {
                        PaginaControleEstoque()
                    }

                    composable(route = "lista_cliente") {
                        val listaViewModel: ListaClientesViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return ListaClientesViewModel(db.clienteDao()) as T
                                }
                            }
                        )
                        ListaClientesScreen(
                            viewModel = listaViewModel,
                            onVoltar = { navController.popBackStack() },
                            onIrParaCadastro = { navController.navigate("cadastro_cliente") }
                        )
                    }

                    composable("relatorios") {
                        val rViewModel: RelatoriosViewModel = viewModel(
                            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as android.app.Application)
                        )
                        PaginaRelatorio(onVoltar = { navController.popBackStack() }, viewModel = rViewModel)
                    }

                    // ROTA DE VENDAS
                    composable(
                        route = "vendas?produtoNome={produtoNome}&preco={preco}&quantidade={quantidade}",
                        arguments = listOf(
                            navArgument("produtoNome") { type = NavType.StringType; defaultValue = "" },
                            navArgument("preco") { type = NavType.StringType; defaultValue = "" },
                            navArgument("quantidade") { type = NavType.IntType; defaultValue = 1 }
                        )
                    ) {


                        // AQUI ESTÁ O CONSERTO: Só passamos o que a função agora pede
                        RegistroVendaScreen(
                            navController = navController,
                            vModel = vendaViewModel
                        )
                    }

                    // ROTA DE ENTRADA DE ESTOQUE (AGORA NO LUGAR CERTO!)
                    composable("entrada_estoque") {
                        val entradaViewModel: EntradaEstoqueViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return EntradaEstoqueViewModel(db.produtoDao()) as T
                                }
                            }
                        )

                        EntradaEstoqueScreen(
                            navController = navController,
                            vModel = entradaViewModel
                        )
                    }
                }
            }
        }
    }
}