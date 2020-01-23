package room.components.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import model.Recordatorio

@Dao
interface RecordatorioDAO {
    @Insert
    fun insert(recordatorio: Recordatorio)

    @Update
    fun update(recordatorio: Recordatorio)

    @Delete
    fun delete(recordatorio: Recordatorio)
}