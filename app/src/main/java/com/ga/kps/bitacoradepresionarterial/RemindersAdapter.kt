package com.ga.kps.bitacoradepresionarterial

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import model.Recordatorio
import org.w3c.dom.Text
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RemindersAdapter(val context: Context?) : ListAdapter<Recordatorio, RemindersAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener{
    private var listener: View.OnClickListener? = null
    private lateinit var array: Array<String>
    private val calendar = Calendar.getInstance()
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    private val sdfDisplayTime = SimpleDateFormat.getTimeInstance(DateFormat.SHORT)

    class DIFF_CALLBACK: DiffUtil.ItemCallback<Recordatorio>(){
        override fun areContentsTheSame(oldItem: Recordatorio, newItem: Recordatorio): Boolean {
            return oldItem.dias_de_semana.equals(newItem.dias_de_semana) && oldItem.hora.equals(newItem.hora) && oldItem.nota.equals(newItem.nota) && oldItem.tipo == newItem.tipo
        }

        override fun areItemsTheSame(oldItem: Recordatorio, newItem: Recordatorio): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val tipoRecordatorioTV = v.findViewById<TextView>(R.id.tipoRecordatorioTV)
        val icomoTipoRecordatorioIV = v.findViewById<ImageView>(R.id.reminderIconIV)
        val horaRecordatorioTV = v.findViewById<TextView>(R.id.horaRecordatorioTV)
        val luTV = v.findViewById<TextView>(R.id.luTV)
        val maTV = v.findViewById<TextView>(R.id.maTV)
        val miTV = v.findViewById<TextView>(R.id.miTV)
        val juTV = v.findViewById<TextView>(R.id.juTV)
        val viTV = v.findViewById<TextView>(R.id.viTV)
        val saTV = v.findViewById<TextView>(R.id.saTV)
        val doTV = v.findViewById<TextView>(R.id.doTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_reminder,parent, false)
        v.setOnClickListener(this)
        array = context?.resources?.getStringArray(R.array.tipos_recordatorio)!!
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recordatorioActual = getItem(position)
            when(recordatorioActual.tipo){

            0 -> {
                holder.tipoRecordatorioTV.text = array[0]
                holder.icomoTipoRecordatorioIV.setImageResource(R.drawable.ic_heart_pulse)
            }
            1 -> {
                holder.tipoRecordatorioTV.text = array[1]
                holder.icomoTipoRecordatorioIV.setImageResource(R.drawable.ic_pill)
            }

        }
        calendar.time = sdf.parse(recordatorioActual.hora)
        holder.horaRecordatorioTV.text = sdfDisplayTime.format(calendar.time)

        val arrayOfDays = recordatorioActual.dias_de_semana.toCharArray()

        for(day in arrayOfDays){
            when(day){
                '1' -> {
                    holder.luTV.setTextColor(Color.WHITE)
                    holder.luTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '2' -> {
                    holder.maTV.setTextColor(Color.WHITE)
                    holder.maTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '3' -> {
                    holder.miTV.setTextColor(Color.WHITE)
                    holder.miTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '4' -> {
                    holder.juTV.setTextColor(Color.WHITE)
                    holder.juTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '5' -> {
                    holder.viTV.setTextColor(Color.WHITE)
                    holder.viTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '6' -> {
                    holder.saTV.setTextColor(Color.WHITE)
                    holder.saTV.setBackgroundResource(R.drawable.text_view_circle)
                }
                '7' -> {
                    holder.doTV.setTextColor(Color.WHITE)
                    holder.doTV.setBackgroundResource(R.drawable.text_view_circle)
                }
            }
        }

    }

    fun getReminderAt(position: Int): Recordatorio{
        return getItem(position)
    }

    fun setOnClickListener(listener: View.OnClickListener){
        this.listener = listener
    }

    override fun onClick(v: View?) {
        listener!!.onClick(v)
    }
}