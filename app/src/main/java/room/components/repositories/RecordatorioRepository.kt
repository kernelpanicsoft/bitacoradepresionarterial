package room.components.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import model.Recordatorio
import room.components.BPADataBase
import room.components.BPADataBase_Impl
import room.components.daos.RecordatorioDAO

class RecordatorioRepository(application: Application){
    val recordatorioDao : RecordatorioDAO

    init {
        val database = BPADataBase.getInstance(application)
        recordatorioDao = database.recordatorioDao()
    }

    fun insert(recordatorio: Recordatorio){

    }

    fun update(recordatorio: Recordatorio){

    }

    fun delete(recordatorio: Recordatorio){

    }

    fun getRecordatoriosUsuario( id: Int) : LiveData<List<Recordatorio>> {
        return recordatorioDao.getRecordatoriosUsuario(id)
    }

    fun getRecordatorioUsuario(id: Int) : LiveData<Recordatorio>{
        return recordatorioDao.getRecordatorio(id)
    }
}