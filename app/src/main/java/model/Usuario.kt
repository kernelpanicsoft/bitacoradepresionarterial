package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuario(@PrimaryKey(autoGenerate = true) var id: Int,
                   var imagen_perfil: String? = null,
                   var nombre: String? = null,
                   var apellidos: String? = null,
                   var fecha_nacimiento: String? = null,
                   var genero: Int? = null)