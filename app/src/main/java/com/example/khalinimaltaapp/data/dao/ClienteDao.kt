package com.example.khalinimaltaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.khalinimaltaapp.data.Cliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    // Ordem para o caixa: "Guarde esse novo cliente no cofre"
    @Insert
    suspend fun inserir(cliente: Cliente)

    // Ordem para o caixa: "Me mostre a lista de todos os clientes que temos"
    @Query("SELECT * FROM clientes ORDER BY nome ASC")
    fun buscarTodos(): Flow<List<Cliente>>

    // Ordem para o caixa: "Apague esse cliente específico"
    @Query("DELETE FROM clientes WHERE id = :id")
    suspend fun deletarPorId(id: Int)
}