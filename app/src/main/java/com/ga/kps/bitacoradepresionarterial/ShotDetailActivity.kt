package com.ga.kps.bitacoradepresionarterial

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import helpers.BloodPressureEvaluatorHelper
import kotlinx.android.synthetic.main.activity_shot_detail.*
import model.Toma
import room.components.viewmodels.TomaViewModel
import helpers.ShotEvaluatorHelper
import java.text.SimpleDateFormat
import java.util.*

class ShotDetailActivity : AppCompatActivity() {
    private var shotID : Int = -1
    private lateinit  var shotsViewModel : TomaViewModel
    private lateinit var shotEvaluatorHelper: ShotEvaluatorHelper
    private lateinit  var shotEvaluation : BloodPressureEvaluatorHelper
    private val calendar = Calendar.getInstance()
    private val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
    private val sdfDisplayDate = SimpleDateFormat.getDateInstance()
    private val sdfDisplayTime = SimpleDateFormat.getTimeInstance()
    private lateinit var shotLiveData :  LiveData<Toma>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shot_detail)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.detalles_de_toma)

        shotID = intent.getIntExtra("SHOT_ID", -1)
        shotsViewModel = ViewModelProviders.of(this).get(TomaViewModel::class.java)

        shotEvaluatorHelper = ShotEvaluatorHelper(this)
        shotEvaluation =  BloodPressureEvaluatorHelper(this)

        shotLiveData  =  shotsViewModel.getToma(shotID)
        shotLiveData.observe(this, Observer {
            populateActivityUI(it)
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
                builder.setTitle(getString(R.string.opciones_de_toma))
                builder.setItems(R.array.opciones_toma, DialogInterface.OnClickListener { _, which ->
                    when (which){
                        0 ->{ }
                        1 ->{
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(getString(R.string.esta_seguro_eliminar_toma))
                            builder.setMessage(getString(R.string.eliminar_toma_mensajes))
                            builder.setPositiveButton(getString(R.string.eliminar)){ dialog, id ->
                                deleteShot()
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

    private fun populateActivityUI(toma: Toma){
        SistolicaTomaTV.text = toma.sistolica.toString()
        DiastolicaTomaTV.text = toma.diastolica.toString()
        PulsoTomaTV.text = toma.pulso.toString()
        NotaLabelTV.text = toma.nota

        limbLabelTV.text = shotEvaluatorHelper.getLimbString(toma.extremidad!!)
        posicionLabelTV.text = shotEvaluatorHelper.getPositionString(toma.posicion!!)
        momentLabelTV.text = shotEvaluatorHelper.getMomentString(toma.momento!!)
        valorationTV.text =  shotEvaluation.getBloodPressureEvaluation(toma.sistolica!!,toma.diastolica!!)
        valorationTV.setTextColor(shotEvaluation.getBloodPressureColor(shotEvaluation.getBloodPressureEvaluation(toma.sistolica!!,toma.diastolica!!)))

        calendar.time = sdf.parse(toma.fecha_hora)

        dateLabelTV.text = sdfDisplayDate.format(calendar.time)
        timeTV.text = sdfDisplayTime.format(calendar.time)

        momentLabelTV.text = shotEvaluatorHelper.getMomentString(toma.momento!!)

    }

    private fun deleteShot(){
        if(shotLiveData.hasObservers()){
            shotLiveData.removeObservers(this)
            shotsViewModel.delete(shotLiveData.value!!)
            finish()
        }
    }
}
