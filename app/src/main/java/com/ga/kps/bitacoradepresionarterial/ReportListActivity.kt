package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_report_list.*
import model.Reporte
import room.components.viewmodels.ReportesViewModel
import java.io.File

class ReportListActivity : AppCompatActivity() {
    lateinit var adapter: ReportsAdapter
    lateinit var fileList: ArrayList<File>
    lateinit var reportesViewModel: ReportesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_list)



        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.reportes_creados)

        val mLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )

        mLayoutManager.stackFromEnd = true
        reportsRV.layoutManager = mLayoutManager

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val usuarioID = sharedPref.getInt("actualUserID", -1)

        reportesViewModel = ViewModelProvider(this).get(ReportesViewModel::class.java)
        reportesViewModel.getReportesUsuario(usuarioID).observe(this, Observer {
            adapter.submitList(it)
        })

        adapter = ReportsAdapter(this, reportesViewModel)
        fileList = getFilesList()

        reportsRV.adapter = adapter

        adapter.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)

            val uriforFile: Uri = FileProvider.getUriForFile(
                this,
                "com.ga.kps.bitacoradepresionarterial",
                adapter.getReportFileAt(reportsRV.getChildAdapterPosition(it)))

            intent.setDataAndType(uriforFile,"application/pdf")
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val title = getString(R.string.abrir_reporte)
            val chooser = Intent.createChooser(intent,title)

            if(intent.resolveActivity(packageManager) != null){
                startActivity(chooser)
            }
        })

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFilesList() : ArrayList<File>{
        val path : String? = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.path
        val directory = File(path!!)
        val files = directory.listFiles()!!.toCollection(ArrayList())
        Log.d("ListaArchivos", files.size.toString())
        return files
   }
}
