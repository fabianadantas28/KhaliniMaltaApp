package com.example.khalinimaltaapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "usuarios") // Isso diz ao Room: "Crie uma tabela chamada usuarios"
data class Usuario(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(), // A "placa" única que você já domina!
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val datahora: String = ""
    )