package com.example.khalinimaltaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.khalinimaltaapp.model.Cliente
import com.example.khalinimaltaapp.ui.theme.KhaliniMaltaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhaliniMaltaAppTheme {
                val navController = rememberNavController()

                // Alteramos a startDestination para "splash"
                NavHost(navController = navController, startDestination = "splash") {

                    // 1. Nova Rota para a Tela de Abertura
                    composable("splash") {
                        SplashScreen(onTimeout = {
                            // Após os 3 segundos, ele navega para o login
                            // O popUpTo garante que o usuário não volte para a splash ao clicar em "voltar"
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        })
                    }

                    // 2. Rota da Tela de Login (Sua rota atual)
                    composable("login") {
                        LoginScreen(onIrParaCadastro = {
                            navController.navigate("cadastro")
                        })
                    }

                    // 3. Rota da Tela de Cadastro
                    composable("cadastro") {
                        CadastroClienteScreen(onContinuar = {
                            navController.navigate("criar_senha")
                        })
                    }

                    // 4. Rota da Tela de Criar Senha
                    composable("criar_senha") {
                        CriarSenhaScreen(onFinalizar = {
                            navController.navigate("login")
                        })
                    }
                }
            }
        }
    }
}