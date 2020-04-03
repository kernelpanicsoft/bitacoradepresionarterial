package model

import androidx.room.ColumnInfo

data class CantidadTomasPorValoracion (
    @ColumnInfo(name = "categoria") val categoria: Int?,
    @ColumnInfo(name = "cantidad") val cantidad: Int?
)