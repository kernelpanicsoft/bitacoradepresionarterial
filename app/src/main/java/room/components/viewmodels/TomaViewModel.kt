package room.components.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import helpers.Filtros
import helpers.Ordenes
import helpers.Valoracion
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


    fun getFilteredShotList(id: Int, filter: Int, order: Int) : LiveData<List<Toma>>{
            //Log.d("GetFilteredShotList",id.toString() + " | "+ filter.toString() + " | " + order.toString())
            when(filter){
                Filtros.PREDETERMINADO -> {
                    shots.addSource(repository.gettomasUsuarioOrdenadas(id,order)){values ->
                        shots.value = values
                    }
                   // Log.d("Filtro", "Filtro predeterminado")
                }
                Filtros.VALORACION -> {
                    shots.addSource(repository.getTomasPorValoracion(id,filter,order)){ values ->
                        shots.value = values
                    }
                  //  Log.d("Filtro", "Filtro Valoracion")
                }
                Filtros.MOMENTO -> {
                    shots.addSource(repository.getTomasPorMomento(id,filter,order)){ values ->
                        shots.value = values
                    }
                    Log.d("Filtro", "Filtro momento")
                }
                Filtros.EXTREMIDAD -> {
                    shots.addSource(repository.getTomasPorExtremidad(id,filter,order)){ values ->
                        shots.value = values
                    }
                 //   Log.d("Filtro", "Filtro Extremidad")
                }
                Filtros.POSICION -> {
                    shots.addSource(repository.getTomasPorPosicion(id,filter,order)){ values ->
                        shots.value = values
                    }
                //    Log.d("Filtro", "Filtro Posicion")
                }
            }

        return shots
    }




}