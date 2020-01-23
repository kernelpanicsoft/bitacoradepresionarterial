package model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = arrayOf(Index("usuario_id")),
    foreignKeys = arrayOf(ForeignKey(entity = Usuario::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("usuario_id"),
        onDelete = ForeignKey.CASCADE
        )
))
data class Toma (@PrimaryKey(autoGenerate = true) val id: Int,
                 var sistolica: Int,
                 var diatolica: Int,
                 var pulso: Int,
                 var fecha_hora: String,
                 var posicion: Int,
                 var extremidad: Int,
                 var nota: String,
                 var momento: Int,
                 var usuario_id: Int
                 )