package room.components

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import model.*
import room.components.daos.RecordatorioDAO
import room.components.daos.TomaDAO
import room.components.daos.UsuarioDAO

@Database(entities = arrayOf(Usuario::class,
    Toma::class,
    Recordatorio::class),
    version = 1)
abstract class BPADataBase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun tomaDao(): TomaDAO
    abstract fun recordatorioDao(): RecordatorioDAO


    companion object{
        @Volatile private var instance: BPADataBase? = null

        fun getInstance(context: Context) : BPADataBase =
            instance ?: synchronized(this){
                instance ?: buildDatabse(context).also{ instance = it}
            }

        private fun buildDatabse(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                BPADataBase::class.java, "BPADatabse")
                .build()
    }

}