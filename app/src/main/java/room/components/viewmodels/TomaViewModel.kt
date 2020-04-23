package room.components.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import helpers.Ordenes
import model.CantidadTomasPorValoracion
import model.Toma
import room.components.repositories.TomaRepository

class TomaViewModel(application: Application) : AndroidViewModel(application) {
    val repository : TomaRepository = TomaRepository(application)

    private val shots = MediatorLiveData<List<Toma>>()


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


    fun getSortedShotList(id: Int, order: Int) : LiveData<List<Toma>>{
        when(order){
            Ordenes.PREDETERMINADO ->{
                    shots.addSource(repository.getTomasUsuario(id)){ values ->
                        shots.value = values
                    }
            }
            Ordenes.ANTIGUEDAD_ASC ->{
                    shots.addSource(repository.getTomasUsuarioAsc(id)){ values->
                        shots.value = values
                    }
            }
            Ordenes.ANTIGUEDAD_DESC ->{
                    shots.addSource(repository.getTomasUsuarioDesc(id)){ values ->
                        shots.value = values
                    }
            }
        }

        return shots
    }


}