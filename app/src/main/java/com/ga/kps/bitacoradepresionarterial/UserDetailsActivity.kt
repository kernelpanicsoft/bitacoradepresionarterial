package com.ga.kps.bitacoradepresionarterial

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import helpers.Genero
import kotlinx.android.synthetic.main.activity_user_details.*
import model.Usuario
import room.components.viewmodels.UsuarioViewModel
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

class UserDetailsActivity : AppCompatActivity() {
    lateinit var usuarioViewModel : UsuarioViewModel
    private val sdfDisplayDate = SimpleDateFormat.getDateInstance()
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    private val calendar = Calendar.getInstance()

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
            R.id.item_edit ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.opciones_de_usuario))
                builder.setItems(R.array.opciones_toma) { _, which ->
                    when(which){
                        0 ->{ }
                        1 ->{
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(getString(R.string.esta_seguro_eliminar_usuario))
                            builder.setMessage(getString(R.string.eliminar_usuario_mensaje))
                            builder.setPositiveButton(getString(R.string.eliminar)) { dialog, id ->

                            }
                            builder.setNegativeButton(getString(R.string.cancelar)) { dialog, id ->

                            }
                            val alertDialog = builder.create()
                            alertDialog.show()
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun populateUserFields(usuario: Usuario){
        nombreTV.text = usuario.nombre
        apellidosTV.text = usuario.apellidos
        calendar.time = sdf.parse(usuario.fecha_nacimiento)
        fechaNacimientoTV.text = sdfDisplayDate.format(calendar.time)
        val edad = getDiffYears(calendar.time,Calendar.getInstance().time)
        edadTV.text = getString(R.string.edad_placeholder, edad)
        when(usuario.genero){
            Genero.MASCULINO->{
                generoTV.text = getString(R.string.masculino)
            }
            Genero.FEMENINO->{
                generoTV.text = getString(R.string.femenino)
            }
        }
    }

    private fun getDiffYears(first: Date, last: Date) : Int{
        val a = getCalendar(first)
        val b = getCalendar(last)
        var diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR)
        if(a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE)){
            diff--
        }
        return diff

    }

    private fun getCalendar(date: Date) : Calendar{
        val cal = Calendar.getInstance(Locale.US)
        cal.time = date
        return cal
    }
}
