package room.components.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import model.Reporte

@Dao
interface ReporteDAO {
    @Insert
    fun insert(reporte: Reporte)

    @Update
    fun update(reporte: Reporte)

    @Delete
    fun delete(reporte: Reporte)

    @Query("SELECT * FROM Reporte WHERE Reporte.usuario_id = :id")
    fun getReportesUsuario(id: Int) : LiveData<List<Reporte>>

}