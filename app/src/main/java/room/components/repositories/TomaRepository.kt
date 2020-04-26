package room.components.repositories

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import model.CantidadTomasPorValoracion
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

    fun getTomasUsuarioAsc(id: Int): LiveData<List<Toma>>{
        return tomaDao.getTomasUsuarioAsc(id)
    }

    fun getTomasUsuarioDesc(id: Int): LiveData<List<Toma>>{
        return tomaDao.getTomasUsuarioDesc(id)
    }

    fun getToma(id: Int) : LiveData<Toma>{
        return tomaDao.getToma(id)
    }

    fun getCantidadPorValoracion(id: Int) : LiveData<List<CantidadTomasPorValoracion>> {
        return tomaDao.getValoracionTomasUsuario(id)
    }

    fun getTomasPorValoracion(id: Int, valoracion: Int, orden: Int) : LiveData<List<Toma>>{
        return tomaDao.getTomasUsuarioPorValoracion(id,valoracion,orden)
    }

    fun getTomasPorMomento(id: Int, momento: Int, orden: Int): LiveData<List<Toma>>{
        return tomaDao.getTomasUsuarioPorMomento(id,momento, orden)
    }

    fun getTomasPorExtremidad(id: Int, extremidad: Int, orden: Int): LiveData<List<Toma>>{
        return tomaDao.getTomasUsuarioPorExtremidad(id,extremidad, orden)
    }

    fun getTomasPorPosicion(id: Int, position: Int, orden: Int): LiveData<List<Toma>>{
        return tomaDao.getTomasUsuarioPorPosicion(id, position,orden)
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