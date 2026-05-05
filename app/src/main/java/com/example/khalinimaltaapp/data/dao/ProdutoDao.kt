package com.example.khalinimaltaapp.data.dao

import androidx.room.*
import com.example.khalinimaltaapp.data.Produto
import kotlinx.coroutines.flow.Flow


@Dao
interface ProdutoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(produto: Produto)

    @Update
    suspend fun atualizar(produto: Produto)

    // ADICIONE ESTA LINHA AQUI:
    @Query("SELECT * FROM produtos WHERE nomeProduto LIKE '%' || :nome || '%'")
    fun buscarPorNome(nome: String): Flow<List<Produto>>

    @Query("SELECT * FROM produtos WHERE categoria = :categoriaDigitada")
    fun buscarPorCategoria(categoriaDigitada: String): Flow<List<Produto>>

    @Query("SELECT * FROM produtos")
    fun getAllProdutos(): Flow<List<Produto>>

    @Delete
    suspend fun deletar(produto: Produto)
}