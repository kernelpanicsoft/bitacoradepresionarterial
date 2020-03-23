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
        onDelete = ForeignKey.CASCADE)
))
data class Recordatorio (@PrimaryKey(autoGenerate = true) val id: Int,
                         var hora: String? = null,
                         var dias_de_semana: String = "",
                         var tipo: Int? = null,
                         var nota: String? = null,
                         var usuario_id: Int? = null)