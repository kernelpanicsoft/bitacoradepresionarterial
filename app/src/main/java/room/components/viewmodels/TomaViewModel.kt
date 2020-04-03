package room.components.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import model.CantidadTomasPorValoracion
import model.Toma
import room.components.repositories.TomaRepository

class TomaViewModel(application: Application) : AndroidViewModel(application) {
    val repository : TomaRepository = TomaRepository(application)

    fun insert(toma: Toma){
        repository.insert(toma)
    }

    fun update(toma: Toma){
        repository.update(toma)
    }

    fun delete(toma: Toma){
        repository.delete(toma)
    }

    fun getTomasUsuario(id: Int) : LiveData<List<Toma>>{
        return repository.getTomasUsuario(id)
    }

    fun getToma(id: Int) : LiveData<Toma>{
        return repository.getToma(id)
    }

    fun getCantidadPorValoracion(id: Int) : LiveData<List<CantidadTomasPorValoracion>>{
        return repository.getCantidadPorValoracion(id)
    }



}