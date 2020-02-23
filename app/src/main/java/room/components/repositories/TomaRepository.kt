package room.components.repositories

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import model.Toma
import room.components.BPADataBase
import room.components.daos.TomaDAO

class TomaRepository(application: Application) {
    val tomaDao: TomaDAO

    init{
        val dataBase = BPADataBase.getInstance(application)
        tomaDao = dataBase.tomaDao()
    }


    fun insert(toma: Toma){
        InsertTomaAsyncTask(tomaDao).execute(toma)
    }

    fun update(toma: Toma){
        UpdateTomaAsyncTask(tomaDao).execute(toma)
    }

    fun delete(toma: Toma){
        DeleteTomaAsyncTask(tomaDao).execute(toma)
    }

    fun getTomasUsuario( id: Int) : LiveData<List<Toma>> {
        return tomaDao.getTomasUsuario(id)
    }

    fun getToma(id: Int) : LiveData<Toma>{
        return tomaDao.getToma(id)
    }

    private class InsertTomaAsyncTask constructor(private val tomaDAO: TomaDAO) : AsyncTask<Toma, Void, Void>(){
        override fun doInBackground(vararg params: Toma): Void? {
            tomaDAO.insert(params[0])
            return null
        }
    }

    private class UpdateTomaAsyncTask constructor(private val tomaDAO: TomaDAO) : AsyncTask<Toma, Void, Void>(){
        override fun doInBackground(vararg params: Toma): Void? {
            tomaDAO.update(params[0])
            return null
        }
    }

    private class DeleteTomaAsyncTask constructor(private val tomaDAO: TomaDAO) : AsyncTask<Toma, Void, Void>(){
        override fun doInBackground(vararg params: Toma): Void? {
            tomaDAO.delete(params[0])
            return null
        }
    }
}