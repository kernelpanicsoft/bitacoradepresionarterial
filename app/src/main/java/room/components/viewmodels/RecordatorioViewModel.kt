package room.components.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import model.Recordatorio
import room.components.repositories.RecordatorioRepository

class RecordatorioViewModel(application: Application) : AndroidViewModel(application)
{
    val repository : RecordatorioRepository = RecordatorioRepository(application)

    fun insert(recordatorio: Recordatorio){
        repository.insert(recordatorio)
    }

    fun update(recordatorio: Recordatorio){
        repository.update(recordatorio)
    }

    fun delete(recordatorio: Recordatorio){
        repository.delete(recordatorio)
    }

    fun getRecordatoriosUsuario(id: Int) : LiveData<List<Recordatorio>>{
        return repository.getRecordatoriosUsuario(id)
    }

    fun getRecordatorio(id: Int) : LiveData<Recordatorio>{
        return repository.getRecordatorioUsuario(id)
    }

    fun getAllRecordatorios() : LiveData<List<Recordatorio>>{
        return repository.getAllRecordatorios()
    }
}