package com.example.khalinimaltaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.khalinimaltaapp.ui.theme.KhaliniMaltaAppTheme
import com.example.khalinimaltaapp.viewmodel.CriarSenhaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhaliniMaltaAppTheme {
                val navController = rememberNavController()
                val sharedViewModel: CriarSenhaViewModel = viewModel()

                NavHost(navController = navController, startDestination = "login") {

                    // Rota de Login (Nomes corrigidos conforme o erro)
                    composable(route = "login") {
                        LoginScreen(
                            onIrParaPaginaInicial = { navController.navigate("home") },
                            onIrParaCadastro = { navController.navigate("cadastro_cliente") }
                        )
                    }

                    // Rota de Cadastro
                    composable(route = "cadastro_cliente") {
                        CadastroClienteScreen(
                            onContinuar = { navController.navigate("criar_senha") },
                            viewModel = sharedViewModel
                        )
                    }

                    // Rota de Senha
                    composable(route = "criar_senha") {
                        CriarSenhaScreen(
                            onFinalizar = { navController.navigate("login") },
                            viewModel = sharedViewModel
                        )
                    }

                    // Outras Rotas
                    composable(route = "home") { PaginaPrincipalKM(onAbrirMenu = { navController.navigate("menu") }) }
                    composable(route = "menu") { MenuScreen(onVoltar = { navController.popBackStack() }) }
                }
            }
        }
    }
}