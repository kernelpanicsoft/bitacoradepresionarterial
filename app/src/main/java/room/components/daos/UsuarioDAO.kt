package room.components.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import model.Usuario

@Dao
interface UsuarioDAO {
    @Insert
    fun insert(usuario: Usuario)

    @Update
    fun update(usuario: Usuario)

    @Delete
    fun delete(usuario: Usuario)

    @Query("SELECT * FROM Usuario")
    fun getAllUsuarios() : LiveData<List<Usuario>>

    @Query("SELECT * FROM Usuario WHERE Usuario.id = :id")
    fun getUsuario( id : Int?) : LiveData<Usuario>

    @Query("SELECT max(id) FROM Usuario")
    fun getLastUserID() : LiveData<Long>


}