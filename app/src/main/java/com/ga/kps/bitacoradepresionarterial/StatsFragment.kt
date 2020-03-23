package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_stats.*
import model.Toma
import room.components.viewmodels.TomaViewModel
import java.text.DecimalFormat
import kotlin.math.min
import kotlin.math.roundToInt

class StatsFragment : Fragment(), OnChartValueSelectedListener {
    lateinit var tomaViewModel: TomaViewModel
    lateinit var shots: LiveData<List<Toma>>
    var currenctShotID : Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_stats, container, false)
        return v
    }

    override fun onResume() {
        super.onResume()

        with(statsChartLC) {
            setTouchEnabled(false)
           // setOnChartValueSelectedListener(this)
            setDrawGridBackground(false)
            description.isEnabled = false
            setScaleEnabled(false)
            setPinchZoom(false)
        }

        val xAxis = statsChartLC.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        statsChartLC.axisRight.setDrawLabels(false)
        statsChartLC.setTouchEnabled(true)
        statsChartLC.setOnChartValueSelectedListener(this)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val usuarioID = sharedPref.getInt("actualUserID", -1)


        tomaViewModel = ViewModelProvider(this).get(TomaViewModel::class.java)
        shots = tomaViewModel.getTomasUsuario(usuarioID)
        shots.observe(viewLifecycleOwner, Observer {
            setData(it)
        })


        detallesTV.setOnClickListener {
            val nav = Intent(context,ShotDetailActivity::class.java)
            nav.putExtra("SHOT_ID", currenctShotID)
            startActivity(nav)
        }
    }

    private fun setData(shotList: List<Toma>){
        val entriesSistolica = ArrayList<Entry>()
        val entriesDiastolica = ArrayList<Entry>()
        val entriesPulso = ArrayList<Entry>()
        val dataSets : ArrayList<ILineDataSet> =  ArrayList()


        for((index,shot) in shotList.withIndex()){
            entriesSistolica.add(Entry(index.toFloat(), shot.sistolica!!.toFloat(), shot.id))
            entriesDiastolica.add(Entry(index.toFloat(), shot.diastolica!!.toFloat(),shot.id))
            entriesPulso.add(Entry(index.toFloat(),shot.pulso!!.toFloat(),shot.id))

        }


        val  dataSetSistolica = LineDataSet(entriesSistolica,getString(R.string.sistolica_label))
        with(dataSetSistolica){
            color = ContextCompat.getColor(context!!,R.color.colorPrimary)
            valueTextColor = ContextCompat.getColor(context!!,R.color.colorPrimary)
            setCircleColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
            setDrawCircleHole(false)
            setDrawValues(false)
        }

        val  dataSetDiastolica = LineDataSet(entriesDiastolica,getString(R.string.diastolica_label))
        with(dataSetDiastolica){
            color = ContextCompat.getColor(context!!,R.color.colorPrimaryDark)
            valueTextColor = ContextCompat.getColor(context!!,R.color.colorPrimaryDark)
            setCircleColor(ContextCompat.getColor(context!!,R.color.colorPrimaryDark))
            setDrawCircleHole(false)
            setDrawValues(false)
        }

        val  dataSetPulso = LineDataSet(entriesPulso,getString(R.string.pulso_label))
        with(dataSetPulso){
            color = ContextCompat.getColor(context!!,R.color.colorAccent)
            valueTextColor = ContextCompat.getColor(context!!,R.color.colorAccent)
            setCircleColor(ContextCompat.getColor(context!!,R.color.colorAccent))
            setDrawCircleHole(false)
            setDrawValues(false)
        }

        dataSets.add(dataSetSistolica)
        dataSets.add(dataSetDiastolica)
        dataSets.add(dataSetPulso)



        val data = LineData(dataSets)
        statsChartLC.data = data

        statsChartLC.invalidate()

    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        tomaViewModel.getToma(e?.data as Int).observe(viewLifecycleOwner, Observer {
            populateShotDataToUI(it)
        })
    }


    private fun populateShotDataToUI(shot: Toma){
        sistolicaValorTV.text = shot.sistolica.toString()
        diastolicaValorTV.text = shot.diastolica.toString()
        pulsoValorTV.text = shot.pulso.toString()
        currenctShotID = shot.id
    }

}