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
                        PaginaPrincipalKM(onAbrirMenu = { navController.navigate("menu") })
                    }

                    composable(route = "menu") {
                        MenuScreen(onVoltar = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}