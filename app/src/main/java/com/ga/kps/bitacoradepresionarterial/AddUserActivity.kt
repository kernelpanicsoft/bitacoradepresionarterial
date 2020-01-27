package com.ga.kps.bitacoradepresionarterial

import android.app.Activity
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.MenuItem
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProviders
import helpers.Genero
import kotlinx.android.synthetic.main.activity_add_user.*
import model.Usuario
import room.components.viewmodels.UsuarioViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddUserActivity : AppCompatActivity() {
    lateinit var usuarioViewModel: UsuarioViewModel
    private val calendario: Calendar = Calendar.getInstance()
    private val sdf: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.registrar_usuario)

        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel::class.java)

        fechaNacimientoBT.setOnClickListener {
            val datePickerFragment = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendario.set(Calendar.YEAR, year)
                calendario.set(Calendar.MONTH, month)
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                //Toast.makeText(this@AnadirCitaMedicaActivity,"Fecha seleccionada: " + sdf.format(calendario.time), Toast.LENGTH_SHORT).show()

                fechaNacimientoBT.text =  sdf.format(calendario.time)
            }, calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH))
            datePickerFragment.show()
        }

        addUserFAB.setOnClickListener {
            val newUser = Usuario(0)
            newUser.imagen_perfil = "aux"
            newUser.nombre = nombreET.text.toString()
            newUser.apellidos = apellidosET.text.toString()
            newUser.fecha_nacimiento = fechaNacimientoBT.text.toString()

            when(findViewById<RadioButton>(generoRG.checkedRadioButtonId)){
                masculinoRB ->{
                    newUser.genero = Genero.MASCULINO
                }
                femeninoRB ->{
                    newUser.genero = Genero.FEMENINO
                }
            }

            saveUserToDB(newUser)
            finish()
        }
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

    private fun saveUserToDB(usuario: Usuario){
        usuarioViewModel.insert(usuario)
        setResult(Activity.RESULT_OK)
    }
}
