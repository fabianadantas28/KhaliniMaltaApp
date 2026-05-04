package com.example.khalinimaltaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.khalinimaltaapp.data.database.AppDatabase
import com.example.khalinimaltaapp.ui.theme.KhaliniMaltaAppTheme
import com.example.khalinimaltaapp.viewmodel.*
import com.example.khalinimaltaapp.ui.relatorio.PaginaRelatorio
// Verifique se os caminhos abaixo estão corretos no seu projeto
import com.example.khalinimaltaapp.ui.estoque.PaginaControleEstoque

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhaliniMaltaAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val db = AppDatabase.getDatabase(context)

                // ViewModel compartilhado para o fluxo de cadastro de cliente
                val sharedViewModel: CadastroClienteViewModel = viewModel()

                NavHost(navController = navController, startDestination = "login") {

                    // 1. ROTA DE LOGIN (Atualizada com NavController para a lógica do João)
                    composable(route = "login") {
                        LoginScreen(
                            navController = navController,
                            onIrParaPaginaInicial = { navController.navigate("home") },
                            onIrParaCadastro = { navController.navigate("cadastro_cliente") }
                        )
                    }

                    // 2. ROTA DE TROCA DE SENHA (Código do João - Primeiro Acesso)
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

                    // 3. ROTA DE CADASTRO DE FUNCIONÁRIO (Código do João - Acesso Admin)
                    composable("cadastro_usuario") {
                        CadastroUsuarioScreen(
                            onVoltar = { navController.popBackStack() }
                        )
                    }

                    // 4. Rota de Cadastro de Cliente (Público)
                    composable(route = "cadastro_cliente") {
                        CadastroClienteScreen(
                            onContinuar = { navController.navigate("criar_senha") },
                            viewModel = sharedViewModel
                        )
                    }

                    // 5. Rota de Criar Senha (Público)
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

                    // 6. Rota da Home
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

                    // 7. Rota do Menu
                    composable("menu") {
                        MenuScreen(
                            onVoltar = { navController.popBackStack() },
                            onNavegar = { rota -> navController.navigate(rota) }
                        )
                    }

                    // 8. Rota de Categorias
                    composable("categorias") {
                        PaginaCategorias(
                            navController = navController,
                            onIrParaCadastroProduto = { navController.navigate("cadastro_produto") }
                        )
                    }

                    // 9. Rota de Cadastro de Produto
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

                    // 10. Rota de Controle de Estoque
                    composable("controle_estoque") {
                        PaginaControleEstoque()
                    }

                    // 11. Rota da Lista de Clientes
                    composable(route = "lista_clientes") {
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

                    // 12. Rota de Relatórios
                    composable("relatorios") {
                        val rViewModel: RelatoriosViewModel = viewModel(
                            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as android.app.Application)
                        )
                        PaginaRelatorio(onVoltar = { navController.popBackStack() }, viewModel = rViewModel)
                    }

                    // 13. Rota de Vendas
                    composable("vendas") {
                        // Aqui usamos o AndroidViewModelFactory para passar o application automaticamente
                        val vendaViewModel: RegistroVendaViewModel = viewModel(
                            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                        )
                        RegistroVendaScreen(navController = navController, vModel = vendaViewModel)
                    }
                    }
                }
            }
        }
    }
