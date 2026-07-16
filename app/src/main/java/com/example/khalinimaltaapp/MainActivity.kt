package com.example.khalinimaltaapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

                // Instanciação limpa dos ViewModels compartilhados usando a extensão nativa do Compose
                val cadastroClienteCompartilhadoVM: CadastroClienteViewModel = viewModel()

                NavHost(navController = navController, startDestination = "splash") {

                    composable(route = "splash") {
                        SplashScreen(onTimeout = {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        })
                    }

                    composable(route = "login") {
                        LoginScreen(
                            navController = navController,
                            sharedViewModel = cadastroClienteCompartilhadoVM,
                            onIrParaPaginaInicial = { navController.navigate("home") },
                            onIrParaCadastro = { navController.navigate("cadastro_cliente") }
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
                            sharedViewModel = cadastroClienteCompartilhadoVM,
                            onNavegar = { rota -> navController.navigate(rota) },
                            onLogout = {
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
                        val vModel: CadastroProdutoViewModel = viewModel()
                        CadastroProdutoScreen(navController = navController, viewModel = vModel)
                    }

                    composable("lista_produtos/{categoriaNome}") { backStackEntry ->
                        val categoria = backStackEntry.arguments?.getString("categoriaNome") ?: ""
                        val vModel: CadastroProdutoViewModel = viewModel()
                        ListaProdutosScreen(
                            navController = navController,
                            categoriaSelecionada = categoria,
                            viewModel = vModel
                        )
                    }

                    composable("controle_estoque") {
                        PaginaControleEstoque(navController = navController)
                    }

                    composable(route = "cadastro_cliente") {
                        CadastroClienteScreen(
                            navController = navController,
                            viewModel = cadastroClienteCompartilhadoVM
                        )
                    }

                    composable(route = "criar_senha") {
                        CriarSenhaScreen(
                            viewModel = cadastroClienteCompartilhadoVM,
                            onFinalizar = {
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(route = "lista_cliente") {
                        val listaViewModel: ListaClientesViewModel = viewModel()
                        ListaClientesScreen(
                            viewModel = listaViewModel,
                            onVoltar = { navController.popBackStack() },
                            onIrParaCadastro = { navController.navigate("cadastro_cliente") }
                        )
                    }

                    composable(route = "cadastro_usuario") {
                        val usuarioViewModel: CadastroUsuarioViewModel = viewModel()
                        CadastroUsuarioScreen(
                            viewModel = usuarioViewModel,
                            onVoltar = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "vendas?produtoNome={produtoNome}&preco={preco}&quantidade={quantidade}",
                        arguments = listOf(
                            navArgument("produtoNome") { type = NavType.StringType; defaultValue = "" },
                            navArgument("preco") { type = NavType.FloatType; defaultValue = 0f },
                            navArgument("quantidade") { type = NavType.IntType; defaultValue = 1 }
                        )
                    ) {
                        val rvViewModel: RegistroVendaViewModel = viewModel()
                        val reciboParaMostrar = rvViewModel.vendaRealizadaParaRecibo

                        if (reciboParaMostrar != null) {
                            ReciboScreen(
                                venda = reciboParaMostrar,
                                onFinalizar = {
                                    rvViewModel.limparRecibo()
                                    navController.popBackStack()
                                }
                            )
                        } else {
                            RegistroVendaScreen(
                                navController = navController,
                                vModel = rvViewModel,
                                clienteViewModel = cadastroClienteCompartilhadoVM
                            )
                        }
                    }

                    composable("relatorios") {
                        val rViewModel: RelatoriosViewModel = viewModel()
                        PaginaRelatorio(onVoltar = { navController.popBackStack() }, viewModel = rViewModel)
                    }

                    composable("entrada_estoque") {
                        val entradaViewModel: EntradaEstoqueViewModel = viewModel()
                        EntradaEstoqueScreen(navController = navController, vModel = entradaViewModel)
                    }

                    composable("gestao_vendas") {
                        val gvViewModel: GestaoVendasViewModel = viewModel()
                        GestaoVendasScreen(onVoltar = { navController.popBackStack() }, viewModel = gvViewModel)
                    }

                    // CORREÇÃO: Alterado argumento para String (para aceitar o email do usuário do Firebase)
                    composable(
                        route = "troca_senha/{usuarioEmail}",
                        arguments = listOf(navArgument("usuarioEmail") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val usuarioEmail = backStackEntry.arguments?.getString("usuarioEmail") ?: ""
                        val tsViewModel: TrocaSenhaViewModel = viewModel()

                        TrocaSenhaScreen(
                            email = usuarioEmail,
                            viewModel = tsViewModel,
                            onSenhaAtualizada = {
                                Toast.makeText(context, "Senha atualizada! Faça login novamente.", Toast.LENGTH_LONG).show()
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("lista_funcionarios") {
                        val funcionariosVM: ListaFuncionariosViewModel = viewModel()
                        ListaFuncionariosScreen(
                            onVoltar = { navController.popBackStack() },
                            onIrParaCadastro = { navController.navigate("cadastro_usuario") },
                            viewModel = funcionariosVM
                        )
                    }
                }
            }
        }
    }
}