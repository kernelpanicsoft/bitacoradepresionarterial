package room.components.repositories

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import model.Reporte
import room.components.BPADataBase
import room.components.daos.ReporteDAO

class ReporteRepository(application: Application) {
    val reportesDAO: ReporteDAO

    init {
        val dataBase = BPADataBase.getInstance(application)
        reportesDAO = dataBase.reporteDao()
    }

    fun insert(reporte: Reporte){
        InsertReportAsyncTask(reportesDAO).execute(reporte)
    }

    fun update(reporte: Reporte){
        UpdateReportAsyncTask(reportesDAO).execute(reporte)
    }

    fun delete(reporte: Reporte){
        DeleteReporteAsyncTask(reportesDAO).execute(reporte)
    }

    fun getReportesUsuario(id: Int) : LiveData<List<Reporte>> {
        return reportesDAO.getReportesUsuario(id)
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