package com.example.khalinimaltaapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.khalinimaltaapp.data.Cliente
import com.example.khalinimaltaapp.data.dao.ClienteDao
import com.example.khalinimaltaapp.model.Usuario
import com.example.khalinimaltaapp.data.dao.UsuarioDao

@Database(entities = [Usuario::class, Cliente::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun clienteDao(): ClienteDao

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
                    .fallbackToDestructiveMigration() // ESTA LINHA SALVA VIDAS!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}