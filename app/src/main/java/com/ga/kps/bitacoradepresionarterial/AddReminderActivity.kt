package com.ga.kps.bitacoradepresionarterial

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import helpers.TipoRecordatorio
import kotlinx.android.synthetic.main.activity_add_reminder.*
import kotlinx.android.synthetic.main.activity_add_reminder.toolbar
import model.Recordatorio
import room.components.viewmodels.RecordatorioViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddReminderActivity : AppCompatActivity() {
    val calendario = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    val sdfHora = SimpleDateFormat("h:mm a")
    lateinit var recordatorio : Recordatorio
    lateinit var recordatorioViewModel : RecordatorioViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.anadir_recordatorio)

        recordatorio = Recordatorio(0)

        ArrayAdapter.createFromResource(this, R.array.tipos_recordatorio,android.R.layout.simple_spinner_item)
            .also{adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tipoRecordatorioSP.adapter = adapter
            }
        tipoRecordatorioSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 ->   {
                        tipoRecordatorioIV.setImageDrawable(getDrawable(R.drawable.ic_heart_pulse))
                        recordatorio.tipo = TipoRecordatorio.TOMA_PRESION
                    }
                    1 ->   {
                        tipoRecordatorioIV.setImageDrawable(getDrawable(R.drawable.ic_pill))
                        recordatorio.tipo = TipoRecordatorio.TOMA_MEDICAMENTO
                    }
                }

            }
        }

        horaRecordatorioBT.setOnClickListener {
            val timePickerFragment = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ view, hourOfDay, minute ->
                calendario.set(Calendar.HOUR_OF_DAY,hourOfDay)
                calendario.set(Calendar.MINUTE, minute)
                //Toast.makeText(this@AnadirCitaMedicaActivity,"Hora seleccionada: " + sdf.format(calendario.time),Toast.LENGTH_SHORT).show()
                horaRecordatorioBT.text = sdfHora.format(calendario.time)
            }, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), DateFormat.is24HourFormat(this))

            timePickerFragment.show()
        }

    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when(view.id){
                R.id.luCB -> {
                    if(checked){
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana + "0"
                    }else{
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana?.replace("0","")
                    }
                }
                R.id.maCB -> {
                    if(checked){
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana + "1"
                    }else{
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana?.replace("1","")
                    }
                }
                R.id.miCB -> {
                    if(checked){
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana + "2"
                    }else{
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana?.replace("2","")
                    }
                }
                R.id.juCB -> {
                    if(checked){
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana + "3"
                    }else{
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana?.replace("3","")
                    }
                }
                R.id.viCB -> {
                    if(checked){
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana + "4"
                    }else{
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana?.replace("4","")
                    }
                }
                R.id.saCB -> {
                    if(checked){
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana + "5"
                    }else{
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana?.replace("5","")
                    }
                }
                R.id.doCB -> {
                    if(checked){
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana + "6"
                    }else{
                        recordatorio.dias_de_semana = recordatorio.dias_de_semana?.replace("6","")
                    }
                }
            }
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


    private fun saveReminder(reminder : Recordatorio){

    }
}
