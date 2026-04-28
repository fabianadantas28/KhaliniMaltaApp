package com.example.khalinimaltaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.khalinimaltaapp.data.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cadastrarUsuario(usuario: Usuario): Unit // Adicione : Unit


    @Query("SELECT * FROM usuarios WHERE email = :email AND senha = :senha LIMIT 1")
    suspend fun realizarLogin(email: String, senha: String): Usuario?

    @Query("SELECT * FROM usuarios")
    fun listarTodosUsuarios(): Flow<List<Usuario>>

    // Altere de String para Int
    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun deletarPorId(id: Int): Unit // Adicione : Unit

}