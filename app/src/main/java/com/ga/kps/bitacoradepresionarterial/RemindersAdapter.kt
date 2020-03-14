package com.ga.kps.bitacoradepresionarterial

import android.content.Context
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

class RemindersAdapter(val context: Context?) : ListAdapter<Recordatorio, RemindersAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener{
    private var listener: View.OnClickListener? = null
    class DIFF_CALLBACK: DiffUtil.ItemCallback<Recordatorio>(){
        override fun areContentsTheSame(oldItem: Recordatorio, newItem: Recordatorio): Boolean {
            return oldItem.dias_de_semana.equals(newItem.dias_de_semana) && oldItem.hora.equals(newItem.hora) && oldItem.nota.equals(newItem.nota) && oldItem.tipo == newItem.tipo
        }

        override fun areItemsTheSame(oldItem: Recordatorio, newItem: Recordatorio): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val tipoRecordatorioIV = v.findViewById<ImageView>(R.id.reminderIconIV)
        val tipoRecordatorioTV = v.findViewById<TextView>(R.id.tipoRecordatorioTV)
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
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recordatorioActual = getItem(position)
        holder.tipoRecordatorioTV.text = recordatorioActual.tipo.toString()
        holder.horaRecordatorioTV.text = recordatorioActual.hora
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