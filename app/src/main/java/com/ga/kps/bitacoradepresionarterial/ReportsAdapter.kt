package com.ga.kps.bitacoradepresionarterial

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*

class ReportsAdapter(val context: Context) : ListAdapter<File,ReportsAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private var listener: View.OnClickListener? = null

    class DIFF_CALLBACK: DiffUtil.ItemCallback<File>(){
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.name == newItem.name && oldItem.path == newItem.path && oldItem.lastModified() == newItem.lastModified()
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.name == newItem.name && oldItem.path == newItem.path && oldItem.lastModified() == newItem.lastModified()
        }
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val nombreReporte = v.findViewById<TextView>(R.id.reportNameTV)
        val fechaReporte = v.findViewById<TextView>(R.id.reportDateTV)
        val opcionesReporte = v.findViewById<ImageView>(R.id.reportOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_report, parent, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reporteActual = getItem(position)
        val lastModDate = Date(reporteActual.lastModified())
        holder.nombreReporte.text = reporteActual.name
        holder.fechaReporte.text = lastModDate.toString()

        holder.opcionesReporte.setOnClickListener {
            val popup = PopupMenu(it.context, it).apply {
                setOnMenuItemClickListener(this@ReportsAdapter)
            }
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu_report, popup.menu)
            popup.show()

        }
    }

    fun getReporteAt(position: Int): File{
        return getItem(position)
    }

    fun setOnClickListener(listAdapter: View.OnClickListener){
        this.listener = listAdapter
    }

    override fun onClick(v: View?) {
        listener!!.onClick(v)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean{
        return when(item.itemId){
            R.id.item_share ->{
                Toast.makeText(context,"Estas compartiendo", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.item_delete ->{
                Toast.makeText(context,"Estas eliminando", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}