package com.example.khalinimaltaapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
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

    // Instanciação direta e protegida usando o escopo correto da Activity
    private val cadastroClienteCompartilhadoVM: CadastroClienteViewModel by lazy {
        val db = AppDatabase.getDatabase(applicationContext)
        CadastroClienteViewModel(db.clienteDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhaliniMaltaAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                val db = remember {
                    try {
                        AppDatabase.getDatabase(context)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Erro ao carregar banco de dados.", Toast.LENGTH_LONG).show()
                        null
                    }
                }

                NavHost(navController = navController, startDestination = "splash") {

                    composable(route = "splash") {
                        SplashScreen(onTimeout = {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        })
                    }

                    composable(route = "login") {
                        if (db != null) {
                            LoginScreen(
                                navController = navController,
                                sharedViewModel = cadastroClienteCompartilhadoVM,
                                onIrParaPaginaInicial = { navController.navigate("home") },
                                onIrParaCadastro = { navController.navigate("cadastro_cliente") }
                            )
                        }
                    }

                    composable("home") {
                        if (db != null) {
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
                    }

                    // SUBSTITUA A ROTA DO MENU NA SUA MAINACTIVITY.KT POR ESTA:
                    composable(route = "menu") {
                        MenuScreen(
                            sharedViewModel = cadastroClienteCompartilhadoVM, // <--- PASSANDO O VM PRONTO AQUI
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
                        if (db != null) {
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
                    }

                    composable("lista_produtos/{categoriaNome}") { backStackEntry ->
                        val categoria = backStackEntry.arguments?.getString("categoriaNome") ?: ""
                        if (db != null) {
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
                    }

                    composable("controle_estoque") {
                        PaginaControleEstoque(navController = navController)
                    }

                    // --- TELAS DE CADASTRO UTILIZANDO O MOTOR COMPARTILHADO ---
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
                        if (db != null) {
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
                    }

                    composable(route = "cadastro_usuario") {
                        if (db != null) {
                            val usuarioViewModel: CadastroUsuarioViewModel = viewModel(
                                factory = object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        @Suppress("UNCHECKED_CAST")
                                        return CadastroUsuarioViewModel(context.applicationContext as android.app.Application) as T
                                    }
                                }
                            )
                            CadastroUsuarioScreen(
                                viewModel = usuarioViewModel,
                                onVoltar = { navController.popBackStack() }
                            )
                        }
                    }

                    // --- ROTA DE VENDAS COM SUPORTE AO RECIBO DINÂMICO ---
                    composable(
                        route = "vendas?produtoNome={produtoNome}&preco={preco}&quantidade={quantidade}",
                        arguments = listOf(
                            navArgument("produtoNome") { type = NavType.StringType; defaultValue = "" },
                            navArgument("preco") { type = NavType.FloatType; defaultValue = 0f },
                            navArgument("quantidade") { type = NavType.IntType; defaultValue = 1 }
                        )
                    ) {
                        if (db != null) {
                            val rvViewModel: RegistroVendaViewModel = viewModel(
                                factory = object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        @Suppress("UNCHECKED_CAST")
                                        return RegistroVendaViewModel(db.produtoDao(), db.vendaDao()) as T
                                    }
                                }
                            )

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
                    }

                    composable("relatorios") {
                        val rViewModel: RelatoriosViewModel = viewModel(
                            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as android.app.Application)
                        )
                        PaginaRelatorio(onVoltar = { navController.popBackStack() }, viewModel = rViewModel)
                    }

                    composable("entrada_estoque") {
                        if (db != null) {
                            val entradaViewModel: EntradaEstoqueViewModel = viewModel(
                                factory = object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        @Suppress("UNCHECKED_CAST")
                                        return EntradaEstoqueViewModel(db.produtoDao()) as T
                                    }
                                }
                            )
                            EntradaEstoqueScreen(navController = navController, vModel = entradaViewModel)
                        }
                    }

                    composable("gestao_vendas") {
                        if (db != null) {
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

                    composable(
                        route = "troca_senha/{usuarioId}",
                        arguments = listOf(navArgument("usuarioId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0

                        if (db != null) {
                            val tsViewModel: TrocaSenhaViewModel = viewModel()

                            TrocaSenhaScreen(
                                usuarioId = usuarioId,
                                viewModel = tsViewModel,
                                onSenhaAtualizada = {
                                    Toast.makeText(context, "Senha atualizada! Faça login novamente.", Toast.LENGTH_LONG).show()
                                    navController.navigate("login") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }

                    composable("lista_funcionarios") {
                        if (db != null) {
                            val funcionariosVM: ListaFuncionariosViewModel = viewModel(
                                factory = object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        @Suppress("UNCHECKED_CAST")
                                        return ListaFuncionariosViewModel(db.usuarioDao()) as T
                                    }
                                }
                            )
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
}