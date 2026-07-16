package com.example.khalinimaltaapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khalinimaltaapp.data.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Removemos o 'usuarioDao' do construtor
class ListaFuncionariosViewModel : ViewModel() {

    // Instância do Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()

    // Puxa todos os funcionários/admins da nuvem em tempo real
    val funcionarios: StateFlow<List<Usuario>> = firestore.collection("usuarios")
        .snapshots()
        .map { querySnapshot ->
            querySnapshot.toObjects(Usuario::class.java)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Função para deletar o funcionário usando o e-mail como identificador exclusivo
    fun excluirFuncionario(email: String) {
        viewModelScope.launch {
            try {
                firestore.collection("usuarios")
                    .document(email.lowercase().trim())
                    .delete()
                    .await()

                Log.d("FIREBASE_SUCCESS", "Usuário $email excluído com sucesso!")
            } catch (e: Exception) {
                Log.e("FIREBASE_ERROR", "Erro ao excluir usuário: ${e.message}")
            }
        }
    }
}