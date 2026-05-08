package com.example.khalinimaltaapp.data.dao

import androidx.room.*
import com.example.khalinimaltaapp.data.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(produto: Produto)

    // O João pediu 'updateProduto', vamos usar este nome para evitar erro nas outras telas dele
    @Update
    suspend fun atualizar(produto: Produto)

    @Update
    suspend fun updateProduto(produto: Produto)

    @Query("SELECT * FROM produtos WHERE nomeProduto LIKE '%' || :nome || '%'")
    fun buscarProdutosPorNome(nome: String): Flow<List<Produto>>

    @Query("SELECT * FROM produtos WHERE categoria = :categoriaDigitada")
    fun buscarPorCategoria(categoriaDigitada: String): Flow<List<Produto>>

    @Query("SELECT * FROM produtos")
    fun getAllProdutos(): Flow<List<Produto>>

    @Query("UPDATE produtos SET qtdeEstoque = :novaQuantidade WHERE id = :id")
    suspend fun atualizarEstoque(id: Int, novaQuantidade: Int)

    @Delete
    suspend fun deletar(produto: Produto)
}