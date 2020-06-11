package room.components.viewmodels

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import model.Reporte
import room.components.BPADataBase
import room.components.daos.ReporteDAO
import room.components.repositories.ReporteRepository

class ReportesViewModel(application: Application): AndroidViewModel(application)  {

    val repository: ReporteRepository = ReporteRepository(application)


    fun insert(reporte: Reporte){
        repository.insert(reporte)
    }

    fun update(reporte: Reporte){
        repository.update(reporte)
    }

    fun delete(reporte: Reporte){
        repository.delete(reporte)
    }

    fun getReportesUsuario(id: Int) : LiveData<List<Reporte>>{
        return repository.getReportesUsuario(id)
    }


}