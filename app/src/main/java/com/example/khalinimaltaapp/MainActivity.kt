package com.example.khalinimaltaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.khalinimaltaapp.ui.theme.KhaliniMaltaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhaliniMaltaAppTheme {
                val navController = rememberNavController()

                // O NavHost controla quem aparece na tela
                NavHost(navController = navController, startDestination = "splash") {

                    // 1. Tela de Abertura (Splash)
                    composable("splash") {
                        SplashScreen(onTimeout = {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        })
                    }

                    // 2. Tela de Login (Ajustada com os dois botões)
                    composable("login") {
                        LoginScreen(
                            onIrParaCadastro = {
                                navController.navigate("cadastro")
                            },
                            onIrParaPaginaInicial = { // <--- AQUI ESTÁ A CORREÇÃO
                                navController.navigate("home")
                            }
                        )
                    }

                    // ... e logo abaixo deve ter isso:
                    composable("cadastro") {
                        CadastroClienteScreen(onContinuar = { /* ... */ })
                    }

                    // 3. Tela de Cadastro
                    composable("cadastro") {
                        CadastroClienteScreen(onContinuar = {
                            navController.navigate("criar_senha")
                        })
                    }

                    // 4. Tela de Criar Senha
                    composable("criar_senha") {
                        CriarSenhaScreen(onFinalizar = {
                            navController.navigate("login")
                        })
                    }

                    // 5. Rota para a Página Inicial (Onde tem o gráfico)
                    composable("home") {
                        PaginaPrincipalKM()
                    }
                }
            }
        }
    }
}