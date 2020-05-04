package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.fragment_stats.*
import model.CantidadTomasPorValoracion
import model.Toma
import room.components.viewmodels.TomaViewModel
import helpers.Filtros
import helpers.Ordenes
import helpers.Valoracion

class StatsFragment : Fragment(), OnChartValueSelectedListener {
    lateinit var tomaViewModel: TomaViewModel
    var currenctShotID : Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
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

        with(distributionPC){
            setEntryLabelTextSize(14f)
            description.isEnabled = false
            legend.isEnabled = true
            isDrawHoleEnabled = false
            setUsePercentValues(true)
            holeRadius = 0f
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.setDrawInside(false)
            setEntryLabelColor(Color.BLACK)

        }

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val usuarioID = sharedPref.getInt("actualUserID", -1)
        val filter = sharedPref.getInt("ShotFilter", Filtros.PREDETERMINADO)
        val order= sharedPref.getInt("ShotsOrder", Ordenes.PREDETERMINADO)

        tomaViewModel = ViewModelProvider(this).get(TomaViewModel::class.java)
        tomaViewModel.getFilteredShotList(usuarioID,filter,order).observe(this, Observer {
            setData(it)

        })

        tomaViewModel.getCantidadPorValoracion(usuarioID).observe(viewLifecycleOwner, Observer {
            setDataForPieChart(it)
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
        tomaViewModel.getToma(e?.data as Int).observe(viewLifecycleOwner,object:   NonNullObserver<Toma>() {
            override fun onNonNullChanged(toma: Toma) {
                populateShotDataToUI(toma)
            }
        })
    }


    private fun populateShotDataToUI(shot: Toma){
        sistolicaValorTV.text = shot.sistolica.toString()
        diastolicaValorTV.text = shot.diastolica.toString()
        pulsoValorTV.text = shot.pulso.toString()
        currenctShotID = shot.id
    }

    private fun setDataForPieChart(shots: List<CantidadTomasPorValoracion>){
        val pieEntries = ArrayList<PieEntry>()
        val colors: ArrayList<Int>  = ArrayList()
        val colorsArray = resources.getIntArray(R.array.colors_for_evaluation)
        //for(a in colorsArray) colors.add(a)

        for(i in shots){
            when(i.categoria){
                Valoracion.HIPOTENSION -> colors.add(context!!.getColor(R.color.hipotension))
                Valoracion.NORMAL -> colors.add(context!!.getColor(R.color.normalBP))
                Valoracion.PREHIPERTENSION -> colors.add(context!!.getColor(R.color.prehipertensionBP))
                Valoracion.HIPERTENSION_1 -> colors.add(context!!.getColor(R.color.hipertension1))
                Valoracion.HIPERTENSION_2 -> colors.add(context!!.getColor(R.color.hipertension2))
                Valoracion.CRISIS -> colors.add(context!!.getColor(R.color.hipertensioncrysis))
            }

            pieEntries.add(
                PieEntry(i.cantidad!!.toFloat(),getNameOfType(i.categoria))

            )
        }

        val dataSet = PieDataSet(pieEntries,"")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueFormatter = PercentFormatter(distributionPC)


        dataSet.colors = colors
        val data = PieData(dataSet)
        distributionPC.data = data
        distributionPC.invalidate()
    }

    private fun getNameOfType(value: Int?) : String{
        val valoracionArray = context?.resources?.getStringArray(R.array.presion_arterial)
        return valoracionArray!![value!!-1000]
    }

    abstract class NonNullObserver<Toma> : Observer<Toma>{
        abstract fun onNonNullChanged(toma: Toma)

        override fun onChanged(t: Toma) {
            t?.let { onNonNullChanged(it) }
        }
    }

    fun getSortedShotListForChart(){
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val usuarioID = sharedPref.getInt("actualUserID", -1)
        val filter = sharedPref.getInt("ShotFilter", Filtros.PREDETERMINADO)
        val order= sharedPref.getInt("ShotsOrder", Ordenes.PREDETERMINADO)

        tomaViewModel.getFilteredShotList(usuarioID,filter,order).observe(this, Observer {
            setData(it)
        })
    }

}