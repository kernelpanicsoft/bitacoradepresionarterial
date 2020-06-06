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

    @Query("SELECT * FROM Toma WHERE Toma.usuario_id = :id ORDER BY date(Toma.fecha_hora) DESC")
    fun getTomasUsuarioDesc(id: Int) : LiveData<List<Toma>>

    @Query("SELECT * FROM Toma WHERE Toma.usuario_id = :id ORDER BY date(Toma.fecha_hora) ASC")
    fun getTomasUsuarioAsc(id: Int) : LiveData<List<Toma>>

    @Query("SELECT * FROM Toma WHERE Toma.usuario_id = :id ")
    fun getTomasUsuario(id: Int) : LiveData<List<Toma>>

    @Query("SELECT * FROM Toma WHERE TOma.usuario_id = :id ORDER BY CASE WHEN :order = 1 THEN date(Toma.fecha_hora) END ASC, CASE WHEN :order = 0 THEN date(Toma.fecha_hora) END DESC")
    fun getTomasUsuarioOrdendas(id: Int, order: Int) : LiveData<List<Toma>>

    @Query("SELECT * FROM Toma WHERE Toma.usuario_id = :id AND Toma.valoracion = :valoracion ORDER BY CASE WHEN :order = 1 THEN date(Toma.fecha_hora) END ASC, CASE WHEN :order = 0 THEN date(Toma.fecha_hora) END DESC")
    fun getTomasUsuarioPorValoracion(id: Int, valoracion: Int, order: Int) : LiveData<List<Toma>>

    @Query("SELECT * FROM Toma WHERE Toma.usuario_id = :id AND Toma.momento = :momento ORDER BY CASE WHEN :order = 1 THEN date(Toma.fecha_hora) END ASC, CASE WHEN :order = 0 THEN date(Toma.fecha_hora) END DESC")
    fun getTomasUsuarioPorMomento(id: Int, momento: Int, order: Int) : LiveData<List<Toma>>

    @Query("SELECT * FROM Toma WHERE Toma.usuario_id = :id AND Toma.extremidad = :extremidad ORDER BY CASE WHEN :order = 1 THEN date(Toma.fecha_hora) END ASC, CASE WHEN :order = 0 THEN date(Toma.fecha_hora) END DESC")
    fun getTomasUsuarioPorExtremidad(id: Int, extremidad: Int, order: Int) : LiveData<List<Toma>>

    @Query("SELECT * FROM Toma WHERE Toma.usuario_id = :id AND Toma.posicion = :posicion ORDER BY CASE WHEN :order = 1 THEN date(Toma.fecha_hora) END ASC, CASE WHEN :order = 0 THEN date(Toma.fecha_hora) END DESC")
    fun getTomasUsuarioPorPosicion(id: Int, posicion: Int, order: Int) : LiveData<List<Toma>>

    @Query("SELECT Toma.valoracion as categoria, COUNT(Toma.valoracion) as cantidad FROM Toma WHERE Toma.usuario_id = :id GROUP BY Toma.valoracion")
    fun getValoracionTomasUsuario(id: Int) : LiveData<List<CantidadTomasPorValoracion>>

    @Query("SELECT * FROM Toma WHERE Toma.usuario_id = :id ORDER BY date(Toma.fecha_hora) DESC")
    fun getTomasUsuarioReporteAsc(id: Int) : List<Toma>
}