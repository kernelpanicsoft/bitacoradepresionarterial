package com.ga.kps.bitacoradepresionarterial

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider

import kotlinx.android.synthetic.main.activity_reminder_detail.*
import model.Recordatorio
import room.components.viewmodels.RecordatorioViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ReminderDetailActivity : AppCompatActivity() {
    private var reminderID : Int = -1
    private lateinit var reminderViewModel: RecordatorioViewModel
    private val calendar = Calendar.getInstance()
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    private val sdfDisplayTime = SimpleDateFormat.getTimeInstance(DateFormat.SHORT)
    private lateinit var reminderLiveData : LiveData<Recordatorio>
    private lateinit var array: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_detail)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.detalles_de_recordatorio)

        reminderID = intent.getIntExtra("REMINDER_ID", -1)
        reminderViewModel = ViewModelProvider(this).get(RecordatorioViewModel::class.java)

        reminderLiveData = reminderViewModel.getRecordatorio(reminderID)
        reminderLiveData.observe(this, androidx.lifecycle.Observer {
            populareActivityUI(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                onBackPressed()
                true
            }
            R.id.item_edit ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.opciones_de_toma))
                builder.setItems(R.array.opciones_toma, DialogInterface.OnClickListener { _, which ->
                    when (which){
                        0 ->{ }
                        1 ->{
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(getString(R.string.esta_seguro_eliminar_recordatorio))
                            builder.setMessage(getString(R.string.eliminar_recordatorio_mensaje))
                            builder.setPositiveButton(getString(R.string.eliminar)){ dialog, id ->
                                deleteReminder()
                            }
                            builder.setNegativeButton(getString(R.string.cancelar)) { dialog, id ->

                            }

                            val alertDialog = builder.create()
                            alertDialog.show()

                        }
                    }
                })
                val alertDialog = builder.create()
                alertDialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    private fun populareActivityUI(reminder: Recordatorio){
        array = resources?.getStringArray(R.array.tipos_recordatorio)!!
        when(reminder.tipo){

            0 -> {
                tipoRecordatorioTV.text = array[0]
                iconoTipoREcordatorioIV.setImageResource(R.drawable.ic_heart_pulse)
            }
            1 -> {
                tipoRecordatorioTV.text = array[1]
                iconoTipoREcordatorioIV.setImageResource(R.drawable.ic_pill)
            }

        }
        calendar.time = sdf.parse(reminder.hora)
        horaRecordatorioTV.text = sdfDisplayTime.format(calendar.time)

        val arrayOfDays = reminder.dias_de_semana.toCharArray()

        for(day in arrayOfDays){
            when(day){
                '0' -> {
                    luTV.setTextColor(Color.WHITE)
                    luTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '1' -> {
                    maTV.setTextColor(Color.WHITE)
                    maTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '2' -> {
                    miTV.setTextColor(Color.WHITE)
                    miTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '3' -> {
                    juTV.setTextColor(Color.WHITE)
                    juTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '4' -> {
                    viTV.setTextColor(Color.WHITE)
                    viTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '5' -> {
                    saTV.setTextColor(Color.WHITE)
                    saTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '6' -> {
                    doTV.setTextColor(Color.WHITE)
                    doTV.setBackgroundResource(R.drawable.text_view_circle)
                }
            }
        }

        notaContentTV.text = reminder.nota
    }

    private fun deleteReminder(){
        if(reminderLiveData.hasObservers()){
            reminderLiveData.removeObservers(this)
            reminderViewModel.delete(reminderLiveData.value!!)
            finish()
        }
    }
}
