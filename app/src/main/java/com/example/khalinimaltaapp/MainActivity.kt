package com.example.khalinimaltaapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhaliniMaltaAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                // Inicialização segura do Banco de Dados com tratamento de erro
                val db = try {
                    AppDatabase.getDatabase(context)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Erro ao carregar banco de dados. Limpe os dados do app.", Toast.LENGTH_LONG).show()
                    null
                }

                // Se o banco falhar, não tentamos renderizar os ViewModels que dependem dele para evitar o crash
                if (db != null) {

                    val sharedViewModel: CadastroClienteViewModel = viewModel()

                    val vendaViewModel: RegistroVendaViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return RegistroVendaViewModel(db.produtoDao(), db.vendaDao()) as T
                            }
                        }
                    )

                    // Verificação de recibo movida para um LaunchedEffect ou observador interno seria o ideal,
                    // mas mantendo sua lógica estável, garantimos que o NavHost inicialize primeiro.
                    NavHost(navController = navController, startDestination = "splash") {

                        composable(route = "splash") {
                            // Se houver uma venda pendente de recibo, redireciona direto
                            if (vendaViewModel.vendaRealizadaParaRecibo != null) {
                                navController.navigate("recibo_tela") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            } else {
                                SplashScreen(onTimeout = {
                                    navController.navigate("login") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                })
                            }
                        }

                        // ROTA DO RECIBO REORGANIZADA DE FORMA SEGURA
                        composable(route = "recibo_tela") {
                            val vendaFinalizada = vendaViewModel.vendaRealizadaParaRecibo
                            if (vendaFinalizada != null) {
                                ReciboScreen(
                                    venda = vendaFinalizada,
                                    onFinalizar = {
                                        vendaViewModel.limparRecibo()
                                        navController.navigate("pagina_categorias") {
                                            popUpTo(0) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            } else {
                                navController.navigate("home") { popUpTo(0) { inclusive = true } }
                            }
                        }

                        composable(route = "login") {
                            LoginScreen(
                                navController = navController,
                                sharedViewModel = sharedViewModel,
                                onIrParaPaginaInicial = { navController.navigate("home") },
                                onIrParaCadastro = { navController.navigate("cadastro_cliente") }
                            )
                        }

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

                        composable("home") {
                            val relViewModel: RelatoriosViewModel = viewModel()
                            PaginaPrincipalKM(
                                onAbrirMenu = { navController.navigate("menu") },
                                onIrParaLogin = {
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                viewModel = relViewModel
                            )
                        }

                        composable(route = "menu") {
                            MenuScreen(
                                onNavegar = { rota -> navController.navigate(rota) },
                                onLogout = {
                                    sharedViewModel.limparParaSair()
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
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
                                        @Suppress("UNCHECKED_CAST")
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
                                        @Suppress("UNCHECKED_CAST")
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
                            PaginaControleEstoque(navController = navController)
                        }

                        composable(route = "lista_cliente") {
                            val listaViewModel: ListaClientesViewModel = viewModel(
                                factory = object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        @Suppress("UNCHECKED_CAST")
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

                        composable(
                            route = "vendas?produtoNome={produtoNome}&preco={preco}&quantidade={quantidade}",
                            arguments = listOf(
                                navArgument("produtoNome") { type = NavType.StringType; defaultValue = "" },
                                navArgument("preco") { type = NavType.StringType; defaultValue = "" },
                                navArgument("quantidade") { type = NavType.IntType; defaultValue = 1 }
                            )
                        ) {
                            RegistroVendaScreen(
                                navController = navController,
                                vModel = vendaViewModel,
                                clienteViewModel = sharedViewModel
                            )
                        }

                        composable("entrada_estoque") {
                            val entradaViewModel: EntradaEstoqueViewModel = viewModel(
                                factory = object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        @Suppress("UNCHECKED_CAST")
                                        return EntradaEstoqueViewModel(db.produtoDao()) as T
                                    }
                                }
                            )
                            EntradaEstoqueScreen(
                                navController = navController,
                                vModel = entradaViewModel
                            )
                        }

                        composable("gestao_vendas") {
                            val gvViewModel: GestaoVendasViewModel = viewModel(
                                factory = object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        @Suppress("UNCHECKED_CAST")
                                        return GestaoVendasViewModel(db.vendaDao()) as T
                                    }
                                }
                            )
                            GestaoVendasScreen(onVoltar = { navController.popBackStack() }, viewModel = gvViewModel)
                        }
                    }
                }
            }
        }
    }
}