package com.example.khalinimaltaapp.data.dao

import androidx.room.* // Certifique-se de importar o Room por completo
import com.example.khalinimaltaapp.data.Produto
import kotlinx.coroutines.flow.Flow
import androidx.room.Update

@Dao
interface ProdutoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(produto: Produto) // Remova o ": Unit", o Kotlin já entende isso

    @Update
    suspend fun updateProduto(produto: Produto) // Essencial para o Registro de Vendas

    @Query("SELECT * FROM produtos WHERE categoria = :categoriaDigitada")
    fun getProdutosPorCategoria(categoriaDigitada: String): Flow<List<Produto>>

    @Query("SELECT * FROM produtos")
    fun getAllProdutos(): Flow<List<Produto>>

    @Delete
    suspend fun deletar(produto: Produto)
}