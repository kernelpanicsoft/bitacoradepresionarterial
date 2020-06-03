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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import model.Reporte
import room.components.viewmodels.ReportesViewModel
import java.io.File
import java.io.IOException
import java.util.*

class ReportsAdapter(val context: Context,val reportesViewModel: ReportesViewModel) : ListAdapter<Reporte,ReportsAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener {
    private var listener: View.OnClickListener? = null

    class DIFF_CALLBACK: DiffUtil.ItemCallback<Reporte>(){
        override fun areItemsTheSame(oldItem: Reporte, newItem: Reporte): Boolean {
            return oldItem.file == newItem.file && oldItem.file == newItem.file && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reporte, newItem: Reporte): Boolean {
            return oldItem.file == newItem.file && oldItem.usuario_id == newItem.usuario_id
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
        val file = getFileForItem(reporteActual.file!!)
        val lastModDate = Date(file.lastModified())
        holder.nombreReporte.text = reporteActual.public_name
        holder.fechaReporte.text = lastModDate.toString()

        holder.opcionesReporte.setOnClickListener {
            val popup = PopupMenu(it.context, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu_report, popup.menu)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                 when(menuItem.itemId){
                    R.id.item_share ->{
                        val intentShareFile = Intent(Intent.ACTION_SEND)
                        val file = File(reporteActual.file)
                        if(file.exists()){
                            intentShareFile.setType("application/pdf")

                            val reportUri: Uri = FileProvider.getUriForFile(
                                context,
                                "com.ga.kps.bitacoradepresionarterial",
                                file
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
                        deleteReport(File(reporteActual.file!!))
                        reportesViewModel.delete(reporteActual)

                        true
                    }
                    else -> false
            }
            }
            popup.show()

        }
    }

    fun getReporteAt(position: Int): Reporte{
        return getItem(position)
    }

    fun getReportFileAt(position: Int): File{
        return File(getItem(position).file)
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


    private fun getFileForItem(path: String) : File{
        return File(path)

    }


}