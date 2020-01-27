package com.ga.kps.bitacoradepresionarterial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import helpers.Genero
import kotlinx.android.synthetic.main.activity_user_details.*
import model.Usuario
import room.components.viewmodels.UsuarioViewModel

class UserDetailsActivity : AppCompatActivity() {
    lateinit var usuarioViewModel : UsuarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.detalles_de_perfil)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val usuarioID = sharedPref.getInt("actualUserID", -1)

        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel::class.java)

       Log.e("Fallo",usuarioID.toString())
        usuarioViewModel.getUsuario(usuarioID).observe(this, Observer {
            populateUserFields(it)
        })



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun populateUserFields(usuario: Usuario){
        nombreTV.text = usuario.nombre
        apellidosTV.text = usuario.apellidos
        fechaNacimientoTV.text = usuario.fecha_nacimiento

        when(usuario.genero){
            Genero.MASCULINO->{
                generoTV.text = getString(R.string.masculino)
            }
            Genero.FEMENINO->{
                generoTV.text = getString(R.string.femenino)
            }
        }
    }
}
