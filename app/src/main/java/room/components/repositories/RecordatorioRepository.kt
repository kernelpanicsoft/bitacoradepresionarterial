package room.components.repositories

import android.app.Application
import android.os.AsyncTask
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
        InsertRecordatorioAsyncTask(recordatorioDao).execute(recordatorio)
    }

    fun update(recordatorio: Recordatorio){
        UpdateRecordatorioAsyncTask(recordatorioDao).execute(recordatorio)
    }

    fun delete(recordatorio: Recordatorio){
        DeleteRecordatorioAsyncTask(recordatorioDao).execute(recordatorio)
    }

    fun getRecordatoriosUsuario( id: Int) : LiveData<List<Recordatorio>> {
        return recordatorioDao.getRecordatoriosUsuario(id)
    }

    fun getRecordatorioUsuario(id: Int) : LiveData<Recordatorio>{
        return recordatorioDao.getRecordatorio(id)
    }

    fun getAllRecordatorios() : LiveData<List<Recordatorio>>{
        return recordatorioDao.getAllRecordatorios()
    }

    private class InsertRecordatorioAsyncTask constructor(private val recordatorioDAO: RecordatorioDAO) : AsyncTask<Recordatorio, Void, Void>(){
        override fun doInBackground(vararg params: Recordatorio): Void? {
            recordatorioDAO.insert(params[0])
            return null
        }
    }

    private  class UpdateRecordatorioAsyncTask constructor(private val recordatorioDAO: RecordatorioDAO) : AsyncTask<Recordatorio, Void, Void>(){
        override fun doInBackground(vararg p0: Recordatorio): Void? {
            recordatorioDAO.update(p0[0])
            return null
        }
    }

    private class DeleteRecordatorioAsyncTask constructor(private val recordatorioDAO: RecordatorioDAO) : AsyncTask<Recordatorio, Void, Void>(){
        override fun doInBackground(vararg p0: Recordatorio): Void? {
            recordatorioDAO.delete(p0[0])
            return null
        }
    }


}