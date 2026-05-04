

package com.example.khalinimaltaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.khalinimaltaapp.data.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cadastrarUsuario(usuario: Usuario)

    @Update
    suspend fun atualizarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE email = :email AND senha = :senha LIMIT 1")
    suspend fun realizarLogin(email: String, senha: String): Usuario?

    @Query("SELECT * FROM usuarios")
    fun listarTodosUsuarios(): Flow<List<Usuario>>

    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun deletarPorId(id: Int)

    // Atualiza apenas a senha e libera o primeiro acesso
    @Query("UPDATE usuarios SET senha = :novaSenha, trocarSenha = 0 WHERE id = :id")
    suspend fun atualizarSenha(id: Int, novaSenha: String)

    // Busca usuários por perfil (ADMIN ou FUNCIONARIO)
    @Query("SELECT * FROM usuarios WHERE perfil = :perfil")
    fun listarPorPerfil(perfil: String): Flow<List<Usuario>>
}