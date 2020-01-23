package room.components.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import model.Usuario
import room.components.repositories.UsuarioRepository

class UsuarioViewModel (application: Application) : AndroidViewModel(application) {
    val repository : UsuarioRepository = UsuarioRepository(application)

    fun insert(usuario: Usuario){
        repository.insert(usuario)
    }

    fun update(usuario: Usuario){
        repository.update(usuario)
    }

    fun delete(usuario: Usuario){
        repository.delete(usuario)
    }

    fun getAllUsuarios() : LiveData<List<Usuario>>{
        return repository.getAllUsuarios()
    }
}