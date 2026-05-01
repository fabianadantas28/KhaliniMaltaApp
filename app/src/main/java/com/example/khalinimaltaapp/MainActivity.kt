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
import com.example.khalinimaltaapp.viewmodel.CadastroClienteViewModel
import com.example.khalinimaltaapp.viewmodel.CadastroProdutoViewModel
import com.example.khalinimaltaapp.viewmodel.ListaClientesViewModel
import com.example.khalinimaltaapp.ui.relatorio.PaginaRelatorio
import com.example.khalinimaltaapp.viewmodel.RelatoriosViewModel
import com.example.khalinimaltaapp.viewmodel.RegistroVendaViewModel


// IMPORTANTE: Verifique se este caminho abaixo é o mesmo onde você salvou a tela de estoque
import com.example.khalinimaltaapp.ui.estoque.PaginaControleEstoque

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhaliniMaltaAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val db = AppDatabase.getDatabase(context)

                // ViewModel compartilhado para o fluxo de cadastro de cliente/senha
                val sharedViewModel: CadastroClienteViewModel = viewModel()

                NavHost(navController = navController, startDestination = "home") {

                    // 1. Rota de Login
                    composable(route = "login") {
                        LoginScreen(
                            onIrParaPaginaInicial = { navController.navigate("home") },
                            onIrParaCadastro = { navController.navigate("cadastro_cliente") }
                        )
                    }

                    // 2. Rota de Cadastro de Cliente
                    composable(route = "cadastro_cliente") {
                        CadastroClienteScreen(
                            onContinuar = { navController.navigate("criar_senha") },
                            viewModel = sharedViewModel
                        )
                    }

                    // 3. Rota de Criar Senha
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

                    // 4. Rota da Home (Página Principal)
                    composable(route = "home") {
                        PaginaPrincipalKM(
                            onAbrirMenu = { navController.navigate("menu") },
                            onIrParaLogin = { navController.navigate("login") }
                        )
                    }

                    // 5. Rota do Menu Principal
                    composable("menu") {
                        MenuScreen(
                            onVoltar = { navController.popBackStack() },
                            onNavegar = { rota ->
                                // Navega para a rota recebida do MenuScreen
                                navController.navigate(rota)
                            }
                        )
                    }

                    // 6. Rota de Categorias
                    composable("categorias") {
                        PaginaCategorias(
                            navController = navController,
                            onIrParaCadastroProduto = { navController.navigate("cadastro_produto") }
                        )
                    }

                    // 7. Rota de Cadastro de Produto
                    composable("cadastro_produto") {
                        val vModel: CadastroProdutoViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return CadastroProdutoViewModel(db.produtoDao()) as T
                                }
                            }
                        )
                        CadastroProdutoScreen(
                            navController = navController,
                            viewModel = vModel
                        )
                    }

                    // 8. Rota de Controle de Estoque (A que unificamos!)
                    composable("controle_estoque") {
                        PaginaControleEstoque()
                    }

                    // 9. Rota da Lista de Clientes
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
                    composable("relatorios") {
                        val context = LocalContext.current
                        // Cria o ViewModel passando o Application necessário
                        val rViewModel: RelatoriosViewModel = viewModel(
                            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as android.app.Application)
                        )

                        PaginaRelatorio(
                            onVoltar = { navController.popBackStack() },
                            viewModel = rViewModel
                        )
                    }
                    composable("vendas") {
                        // Criamos o ViewModel injetando o DAO do banco de dados
                        val vendaViewModel: RegistroVendaViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    // Aqui passamos o produtoDao para o ViewModel conseguir atualizar o estoque
                                    return RegistroVendaViewModel(db.produtoDao()) as T
                                }
                            }
                        )

                        // Chamamos a tela passando o ViewModel configurado
                        RegistroVendaScreen(
                            navController = navController,
                            vModel = vendaViewModel
                        )
                    }
                    }


                } // Fim do NavHost
            } // Fim do Theme
        } // Fim do setContent
    } // Fim do onCreate
