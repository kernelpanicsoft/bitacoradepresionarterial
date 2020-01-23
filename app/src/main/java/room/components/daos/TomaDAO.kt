package room.components.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import model.Toma

@Dao
interface TomaDAO {
    @Insert
    fun insert(toma: Toma)

    @Update
    fun update(toma: Toma)

    @Delete
    fun delete(toma: Toma)
}