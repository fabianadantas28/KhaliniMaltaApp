package com.example.khalinimaltaapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.khalinimaltaapp.data.Produto
import kotlinx.coroutines.flow.Flow

@Dao // Não esqueça dessa anotação, ela é o que faz a mágica funcionar!
interface ProdutoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(produto: Produto): Unit // Adicione : Unit


    @Query("SELECT * FROM produtos WHERE categoria = :categoriaDigitada")
    fun getProdutosPorCategoria(categoriaDigitada: String): Flow<List<Produto>>

    @Query("SELECT * FROM produtos")
    fun getAllProdutos(): Flow<List<Produto>>

    @Delete
    suspend fun deletar(produto: Produto): Unit // Adicione : Unit

}