package com.example.khalinimaltaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.khalinimaltaapp.data.Venda
import kotlinx.coroutines.flow.Flow

@Dao
interface VendaDao {

    @Insert
    suspend fun registrarVenda(venda: Venda): Long

    @Query("SELECT * FROM vendas ORDER BY id DESC")
    fun listarTodasVendas(): Flow<List<Venda>>

    @Query("SELECT * FROM vendas WHERE nomeCliente LIKE '%' || :nome || '%'")
    fun buscarPorCliente(nome: String): Flow<List<Venda>>

    @Query("SELECT SUM(valorTotal) FROM vendas")
    fun totalVendas(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM vendas")
    fun totalQuantidadeVendas(): Flow<Int>
}
