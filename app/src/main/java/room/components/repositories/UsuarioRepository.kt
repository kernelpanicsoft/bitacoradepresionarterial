package room.components.repositories

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import model.Usuario
import room.components.BPADataBase
import room.components.daos.UsuarioDAO

class UsuarioRepository (application: Application){
    val usuarioDao: UsuarioDAO

    init{
        val dataBase = BPADataBase.getInstance(application)
        usuarioDao = dataBase.usuarioDao()
    }

    fun insert(usuario: Usuario){
        InsertUsuarioAsyncTask(usuarioDao).execute(usuario)
    }

    fun update(usuario: Usuario){
        UpdateUsuarioAsyncTask(usuarioDao).execute(usuario)
    }

    fun delete(usuario: Usuario){
        DeleteUsuaurioAsyncTask(usuarioDao).execute(usuario)
    }

    fun getAllUsuarios() : LiveData<List<Usuario>>{
        return usuarioDao.getAllUsuarios()
    }

    fun getUsuario( id : Int) : LiveData<Usuario>{
        return usuarioDao.getUsuario(id)
    }

    fun getLastInsertedUserID() : LiveData<Long>{
        return usuarioDao.getLastUserID()
    }



    private class InsertUsuarioAsyncTask constructor(private val usuarioDAO: UsuarioDAO) : AsyncTask<Usuario, Void, Void>(){
        override fun doInBackground(vararg params: Usuario): Void? {
            usuarioDAO.insert(params[0])
            return null
        }
    }

    private class UpdateUsuarioAsyncTask constructor(private val usuarioDAO: UsuarioDAO) : AsyncTask<Usuario, Void, Void>(){
        override fun doInBackground(vararg params: Usuario): Void? {
            usuarioDAO.update(params[0])
            return null
        }
    }

    private class DeleteUsuaurioAsyncTask constructor(private val usuarioDAO: UsuarioDAO) : AsyncTask<Usuario, Void, Void>(){
        override fun doInBackground(vararg params: Usuario): Void? {
            usuarioDAO.delete(params[0])
            return null
        }
    }
}