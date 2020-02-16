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
                 var sistolica: Int? = null,
                 var diastolica: Int? = null,
                 var pulso: Int? = null,
                 var fecha_hora: String? = null,
                 var posicion: Int? = null,
                 var extremidad: Int? = null,
                 var nota: String? = null,
                 var momento: Int? = null,
                 var usuario_id: Int ? = null
                 )