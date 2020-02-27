package com.ga.kps.bitacoradepresionarterial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import helpers.BloodPressureEvaluatorHelper

import model.Toma
import java.text.SimpleDateFormat
import java.util.*

class ShotsAdapter(val context: Context?) : ListAdapter<Toma, ShotsAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener {
    private var listener: View.OnClickListener? = null
    private val shotEvaluation : BloodPressureEvaluatorHelper = BloodPressureEvaluatorHelper(context!!)
    private val calendar = Calendar.getInstance()
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    private val sdfDisplay = SimpleDateFormat.getDateTimeInstance()


    class DIFF_CALLBACK: DiffUtil.ItemCallback<Toma>(){
        override fun areContentsTheSame(oldItem: Toma, newItem: Toma): Boolean {
            return oldItem.sistolica == newItem.sistolica && oldItem.diastolica == newItem.diastolica && oldItem.fecha_hora == newItem.fecha_hora
        }

        override fun areItemsTheSame(oldItem: Toma, newItem: Toma): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val sistolicaTV = v.findViewById<TextView>(R.id.valorSistolicaTV)
        val diastolicaTV = v.findViewById<TextView>(R.id.valorDiastolicaTV)
        val pulsoTV = v.findViewById<TextView>(R.id.valorPulsoTV)
        val valoracionTV = v.findViewById<TextView>(R.id.valoracionTV)
        val fechaHotaTV = v.findViewById<TextView>(R.id.fechaHoraTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_shoot,parent,false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tomaActual = getItem(position)
        holder.sistolicaTV.text = tomaActual.sistolica.toString()
        holder.diastolicaTV.text = tomaActual.diastolica.toString()
        holder.pulsoTV.text = tomaActual.pulso.toString()
        holder.valoracionTV.text = shotEvaluation.getBloodPressureEvaluation(tomaActual.sistolica!!,tomaActual.diastolica!!)
        holder.valoracionTV.setTextColor(shotEvaluation.getBloodPressureColor(shotEvaluation.getBloodPressureEvaluation(tomaActual.sistolica!!,tomaActual.diastolica!!)))

        calendar.time = sdf.parse(tomaActual.fecha_hora)
        holder.fechaHotaTV.text = sdfDisplay.format(calendar.time)

    }

    fun getShotAt(postion: Int): Toma{
        return getItem(postion)
    }

    fun setOnClickListener(listener: View.OnClickListener){
        this.listener = listener
    }

    override fun onClick(v: View?) {
        listener!!.onClick(v)
    }

    
}