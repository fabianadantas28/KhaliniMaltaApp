package com.example.khalinimaltaapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.khalinimaltaapp.data.dao.ClienteDao
import com.example.khalinimaltaapp.data.Usuario
import com.example.khalinimaltaapp.data.dao.UsuarioDao
import com.example.khalinimaltaapp.data.Cliente
import com.example.khalinimaltaapp.data.Produto // Garanta que este import existe
import com.example.khalinimaltaapp.data.dao.ProdutoDao
import com.example.khalinimaltaapp.data.Venda //
import com.example.khalinimaltaapp.data.dao.VendaDao

// ADICIONEI O Produto::class AQUI ABAIXO:
@Database(
    entities = [Usuario::class, Cliente::class, Produto::class, Venda::class],
    version = 5,  // era 4, agora é 5
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun clienteDao(): ClienteDao
    abstract fun produtoDao(): ProdutoDao
    abstract fun vendaDao(): VendaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "khalini_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}