package room.components.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import model.CantidadTomasPorValoracion
import model.Toma

@Dao
interface TomaDAO {
    @Insert
    fun insert(toma: Toma)

    @Update
    fun update(toma: Toma)

    @Delete
    fun delete(toma: Toma)

    @Query("SELECT * FROM Toma")
    fun getallTomas() : LiveData<List<Toma>>

    @Query("SELECT * FROM Toma WHERE Toma.id = :id")
    fun getToma(id: Int?) : LiveData<Toma>

    @Query("SELECT * FROM Toma WHERE Toma.usuario_id = :id")
    fun getTomasUsuario(id: Int) : LiveData<List<Toma>>

    @Query("SELECT Toma.valoracion as categoria, COUNT(Toma.valoracion) as cantidad FROM Toma WHERE Toma.usuario_id = :id GROUP BY Toma.valoracion")
    fun getValoracionTomasUsuario(id: Int) : LiveData<List<CantidadTomasPorValoracion>>


}