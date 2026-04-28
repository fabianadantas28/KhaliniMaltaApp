package com.example.khalinimaltaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.khalinimaltaapp.data.Cliente

@Dao
interface ClienteDao {

    @Insert
    suspend fun inserir(cliente: Cliente): Unit // Adicione : Unit

    // O nome aqui PRECISA ser igual ao que o ViewModel chama
    @Query("SELECT * FROM clientes ORDER BY nome ASC")
    fun getAllClientes(): Flow<List<Cliente>>

    @Query("DELETE FROM clientes WHERE id = :id")
    suspend fun deletarPorId(id: Int): Unit // Adicione : Unit

}