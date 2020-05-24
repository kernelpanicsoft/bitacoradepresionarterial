package com.ga.kps.bitacoradepresionarterial

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.util.*

class ReportsAdapter(val context: Context) : ListAdapter<File,ReportsAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener {
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
            val popup = PopupMenu(it.context, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu_report, popup.menu)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                 when(menuItem.itemId){
                    R.id.item_share ->{
                        val intentShareFile = Intent(Intent.ACTION_SEND)
                        if(reporteActual.exists()){
                            intentShareFile.setType("application/pdf")

                            val reportUri: Uri = FileProvider.getUriForFile(
                                context,
                                "com.ga.kps.bitacoradepresionarterial",
                                reporteActual
                            )

                            intentShareFile.putExtra(Intent.EXTRA_STREAM, reportUri)
                            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.compartir_reporte))
                            intentShareFile.putExtra(Intent.EXTRA_TEXT,context.getString(R.string.compartir_reporte))
                            context.startActivity(Intent.createChooser(intentShareFile, context.getString(R.string.compartir_reporte_intent)))
                        }
                        true
                    }
                    R.id.item_delete ->{
                        menuItem.menuInfo
                       // deleteReport(reporteActual)
                        Log.d("ListaReportes",this.currentList.size.toString() + " | " + position)
                       // var list = currentList
                       // list.removeAt(position)
                       // notifyItemRemoved(position)

                        true
                    }
                    else -> false
            }
            }
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


    @Throws(IOException::class)
    private fun deleteReport(reportFile: File){
        val reportUri: Uri = FileProvider.getUriForFile(
            context,
            "com.ga.kps.bitacoradepresionarterial",
            reportFile
        )

        context.contentResolver.delete(reportUri,null,null)
    }


}