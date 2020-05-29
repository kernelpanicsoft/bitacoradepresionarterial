package room.components.viewmodels

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import model.Reporte
import room.components.BPADataBase
import room.components.daos.ReporteDAO

class ReportesViewModel(application: Application): AndroidViewModel(application)  {
    val reportesDao: ReporteDAO

    init {
        val dataBase = BPADataBase.getInstance(application)
        reportesDao = dataBase.reporteDao()
    }

    fun insert(reporte: Reporte){
        InsertReportAsyncTask(reportesDao).execute(reporte)
    }

    fun update(reporte: Reporte){
        UpdateReportAsyncTask(reportesDao).execute(reporte)
    }

    fun delete(reporte: Reporte){
        DeleteReporteAsyncTask(reportesDao).execute(reporte)
    }

    fun getReportesUsuario(id: Int) : LiveData<List<Reporte>>{
        return reportesDao.getReportesUsuario(id)
    }

    private class InsertReportAsyncTask constructor(private val reporteDAO: ReporteDAO) : AsyncTask<Reporte, Void, Void>(){
        override fun doInBackground(vararg p0: Reporte): Void? {
           reporteDAO.insert(p0[0])
            return null
        }
    }

    private class UpdateReportAsyncTask constructor(private val reporteDAO: ReporteDAO) : AsyncTask<Reporte, Void, Void>(){
        override fun doInBackground(vararg p0: Reporte): Void? {
            reporteDAO.update(p0[0])
            return null
        }
    }

    private class DeleteReporteAsyncTask constructor(private val reporteDAO: ReporteDAO) : AsyncTask<Reporte, Void, Void>(){
        override fun doInBackground(vararg p0: Reporte): Void? {
            reporteDAO.delete(p0[0])
            return null
        }
    }


}