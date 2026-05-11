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

    // 1. Buscamos pelo ID decrescente para garantir que a última venda apareça primeiro
    @Query("SELECT * FROM vendas ORDER BY id DESC")
    fun listarTodasVendas(): Flow<List<Venda>>

    @Query("SELECT * FROM vendas WHERE nomeCliente LIKE '%' || :nome || '%'")
    fun buscarPorCliente(nome: String): Flow<List<Venda>>

    @Query("SELECT SUM(valorTotal) FROM vendas")
    fun totalVendas(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM vendas")
    fun totalQuantidadeVendas(): Flow<Int>

    // 2. CORREÇÃO DO ERRO:
    // Se o seu app deu erro no campo 'data', vamos ordenar pelo ID
    // que também garante a ordem cronológica correta e não quebra o sistema.
    @Query("SELECT * FROM vendas ORDER BY id DESC")
    fun buscarTodasVendas(): Flow<List<Venda>>
}