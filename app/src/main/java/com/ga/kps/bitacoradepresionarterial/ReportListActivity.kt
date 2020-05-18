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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_report_list.*
import java.io.File

class ReportListActivity : AppCompatActivity() {

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

        val adapter = ReportsAdapter(this)
        adapter.submitList(getFilesList())

        reportsRV.adapter = adapter

        adapter.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)

            val uriforFile: Uri = FileProvider.getUriForFile(
                this,
                "com.ga.kps.bitacoradepresionarterial",
                adapter.getReporteAt(reportsRV.getChildAdapterPosition(it)))

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.item_edit -> {
                getFilesList()
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
