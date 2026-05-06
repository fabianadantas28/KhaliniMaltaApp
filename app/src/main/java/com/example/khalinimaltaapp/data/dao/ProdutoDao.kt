package com.example.khalinimaltaapp.data.dao

import androidx.room.*
import com.example.khalinimaltaapp.data.Produto
import kotlinx.coroutines.flow.Flow // Importando o Flow aqui

@Dao
interface ProdutoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(produto: Produto)

    @Update
    suspend fun atualizar(produto: Produto)

    // Removi o 'kotlinx.coroutines.flow.' e deixei só 'Flow'

    // SEM suspend (porque tem Flow no final)

    @Query("SELECT * FROM produtos WHERE nomeProduto LIKE '%' || :nome || '%'")
    fun buscarProdutosPorNome(nome: String): Flow<List<Produto>>

    @Query("SELECT * FROM produtos WHERE categoria = :categoriaDigitada")
    fun buscarPorCategoria(categoriaDigitada: String): Flow<List<Produto>>


    @Query("SELECT * FROM produtos")
    fun getAllProdutos(): Flow<List<Produto>>
    
    // COM suspend (porque NÃO tem Flow, é uma ação)
    @Query("UPDATE produtos SET qtdeEstoque = :novaQuantidade WHERE id = :id")
    suspend fun atualizarEstoque(id: Int, novaQuantidade: Int)

    @Delete
    suspend fun deletar(produto: Produto)
}