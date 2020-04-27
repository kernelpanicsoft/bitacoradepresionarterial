package com.ga.kps.bitacoradepresionarterial

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import helpers.BloodPressureEvaluatorHelper
import helpers.Extremidad
import helpers.Momento
import helpers.Posicion
import kotlinx.android.synthetic.main.activity_add_shot.*
import model.Toma
import room.components.viewmodels.TomaViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddShotActivity : AppCompatActivity() {
    val calendario = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    val sdfHora = SimpleDateFormat("h:mm a")
    val sdfCalendario = SimpleDateFormat("EEE, MMM d, yyyy")
    lateinit var evaluator : BloodPressureEvaluatorHelper
    var posicion = 0
    var extremidad = 0
    var momento = 0

    lateinit var tomasViewModel : TomaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shot)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.anadir_toma)

        evaluator = BloodPressureEvaluatorHelper(this)
        tomasViewModel = ViewModelProvider(this).get(TomaViewModel::class.java)


        fechaBT.text = sdfCalendario.format(calendario.time)
        horaBT.text = sdfHora.format(calendario.time)

        ArrayAdapter.createFromResource(this, R.array.extremidades,android.R.layout.simple_spinner_item)
            .also{ adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                extremidadSP.adapter = adapter
            }

        extremidadSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> extremidad = Extremidad.BRAZO_IZQUIERDO
                    1 -> extremidad = Extremidad.BRAZO_DERECHO
                    2 -> extremidad = Extremidad.MUNECA_IZQUIERDA
                    3 -> extremidad = Extremidad.MUNECA_DERECHA
                    4 -> extremidad = Extremidad.TOBILLO_IZQUIERDO
                    5 -> extremidad = Extremidad.TOBILLO_DERECHO
                    6 -> extremidad = Extremidad.MUSLO_IZQUIERDO
                    7 -> extremidad = Extremidad.MUSLO_DERECHO
                }

            }
        }


        ArrayAdapter.createFromResource(this, R.array.posiciones,android.R.layout.simple_spinner_item)
            .also{ adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                posicionSP.adapter = adapter
            }

        posicionSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> posicion = Posicion.SENTADO
                    1 -> posicion = Posicion.ACOSTADO
                    2 -> posicion = Posicion.RECOSTADO
                    3 -> posicion = Posicion.DE_PIE
                }
            }
        }

        ArrayAdapter.createFromResource(this, R.array.momento,android.R.layout.simple_spinner_item)
            .also{ adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                momentoDiaSP.adapter = adapter
            }

        momentoDiaSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> momento = Momento.ALEATORIO
                    1 -> momento = Momento.DESPUES_DESPERTAR
                    2 -> momento = Momento.ANTES_DORMIR
                    3 -> momento = Momento.ANTES_MEDICAMENTO
                    4 -> momento = Momento.DESPUES_MEDICAMENTO
                    5 -> momento = Momento.ANTES_COMER
                    6 -> momento = Momento.DESPUES_COMER
                }
                momento = position
            }
        }

        fechaBT.setOnClickListener {
            val datePickerFragment = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendario.set(Calendar.YEAR, year)
                calendario.set(Calendar.MONTH, month)
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                //Toast.makeText(this@AnadirCitaMedicaActivity,"Fecha seleccionada: " + sdf.format(calendario.time), Toast.LENGTH_SHORT).show()
                fechaBT.text = sdfCalendario.format(calendario.time)
            }, calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH))
            datePickerFragment.show()
        }

        horaBT.setOnClickListener {
            val timePickerFragment = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ view, hourOfDay, minute ->
                calendario.set(Calendar.HOUR_OF_DAY,hourOfDay)
                calendario.set(Calendar.MINUTE, minute)
                //Toast.makeText(this@AnadirCitaMedicaActivity,"Hora seleccionada: " + sdf.format(calendario.time),Toast.LENGTH_SHORT).show()
                horaBT.text = sdfHora.format(calendario.time)
            }, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), DateFormat.is24HourFormat(this))

            timePickerFragment.show()
        }


        saveShootFAB.setOnClickListener {

            if(sistolicaET.text.isNullOrEmpty() || diastolicaET.text.isNullOrEmpty() || pulsoET.text.isNullOrEmpty()){
                Snackbar.make(it,getString(R.string.es_necesario_especificar_sis_dia_pul), Snackbar.LENGTH_LONG).show()
            }else{
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                val usuarioID = sharedPref.getInt("actualUserID", -1)

                val toma = Toma(0)
                toma.usuario_id = usuarioID
                toma.sistolica = sistolicaET.text.toString().toInt()
                toma.diastolica = diastolicaET.text.toString().toInt()
                toma.pulso = pulsoET.text.toString().toInt()
                toma.extremidad = extremidad
                toma.posicion = posicion
                toma.momento = momento
                toma.fecha_hora = sdf.format(calendario.time)
                toma.nota = notaET.text.toString()
                toma.valoracion = evaluator.getBloodPressureEvalutationForDB (sistolicaET.text.toString().toInt(), diastolicaET.text.toString().toInt() )
                tomasViewModel.insert(toma)

                finish()

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
}
