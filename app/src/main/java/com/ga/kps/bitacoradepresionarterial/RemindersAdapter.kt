package com.ga.kps.bitacoradepresionarterial

import android.content.Context
import android.view.View
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import model.Recordatorio

class RemindersAdapter(val context: Context?) : ListAdapter<Recordatorio, RemindersAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener{

    class DIFF_CALLBACK: DiffUtil.ItemCallback<Recordatorio>(){
        override fun areContentsTheSame(oldItem: Recordatorio, newItem: Recordatorio): Boolean {
            return oldItem.dias_de_semana.equals(newItem.dias_de_semana) && oldItem.hora.equals(newItem.hora) && oldItem.nota.equals(newItem.nota) && oldItem.tipo == newItem.tipo
        }

        override fun areItemsTheSame(oldItem: Recordatorio, newItem: Recordatorio): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

    }
}