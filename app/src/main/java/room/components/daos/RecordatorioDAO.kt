package room.components.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import model.Recordatorio

@Dao
interface RecordatorioDAO {
    @Insert
    fun insert(recordatorio: Recordatorio)

    @Update
    fun update(recordatorio: Recordatorio)

    @Delete
    fun delete(recordatorio: Recordatorio)

    @Query("SELECT * FROM Recordatorio WHERE Recordatorio.id = :id")
    fun getRecordatorio(id: Int?) : LiveData<Recordatorio>

    @Query("SELECT * FROM Recordatorio WHERE Recordatorio.usuario_id = :id")
    fun getRecordatoriosUsuario(id: Int) : LiveData<List<Recordatorio>>

    @Query("SELECT * FROM RECORDATORIO")
    fun getAllRecordatorios() : LiveData<List<Recordatorio>>
}