package com.ga.kps.bitacoradepresionarterial

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import helpers.*
import kotlinx.android.synthetic.main.activity_main.*
import notifications.system.AlarmsManagerForReminder
import notifications.system.NotificationsManager
import notifications.system.NotificationsManagerForReminders
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: ViewPagerAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSupportActionBar(toolbar)
        title = getString(R.string.app_name)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)

        setupViewPager(ViewPagerPrincipal)
        TabLayoutPrincipal.setupWithViewPager(ViewPagerPrincipal)

        ViewPagerPrincipal.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if(position == 0 || position == 2){
                    addShotFAB.show()
                }else{
                    addShotFAB.hide()
                }
            }
        })

        addShotFAB.setOnClickListener {
            when(ViewPagerPrincipal.currentItem){
                0 ->{
                    val nav = Intent(this, AddShotActivity::class.java)
                    startActivity(nav)
                }
                2 ->{
                    val nav = Intent(this, AddReminderActivity::class.java)
                    startActivity(nav)
                }
            }


        }

        nav_view.setNavigationItemSelectedListener { menuItem ->
            drawer_layout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_account ->{
                    val navProfile = Intent(this, UserDetailsActivity::class.java)
                    startActivityForResult(navProfile, ELIMINAR_USUARIO)
                }
                R.id.nav_change_user ->{
                    val navChangeUser = Intent(this, UserListActivity::class.java)
                    startActivity(navChangeUser)
                }
                R.id.nav_settings ->{
                    val nav = Intent(this, SettingsActivity::class.java)
                    startActivity(nav)
                }

                //R.id.nav_test ->{
                 //   val calendar = Calendar.getInstance()

                  //  val alarm = AlarmsManagerForReminder(this)
                  //  alarm.createAlarmForNotification(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE+1), 12)
                  //  Intent(this, RemindersSetupIntentService::class.java).also {intent ->
                  //      startService(intent)
                  //  }  calendar.get(Calendar.DAY_OF_WEEK).toString()
                  //  Log.d("DiaDeHoy", Calendar.SATURDAY.toString())
                //}

            }
            true
        }
    }

    override fun onResume() {
        super.onResume()

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val usuarioID = sharedPref.getInt("actualUserID", -1)

        if(usuarioID == -1){
            val nav = Intent(this, UserListActivity::class.java)
            startActivity(nav)
        }



    }

    private fun setupViewPager(pager: ViewPager){
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ShotsFragment(),getString(R.string.tomas))
        adapter.addFragment(StatsFragment(),getString(R.string.estadisticas))
        adapter.addFragment(RemindersFragment(),getString(R.string.recordatorios))

        pager.adapter = adapter


    }

    private inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()


        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentTitleList.size
        }

        fun addFragment(fragment: Fragment, title: String){
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)

        }

        override fun getPageTitle(position: Int): CharSequence{
            return mFragmentTitleList[position]
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean{
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                drawer_layout.openDrawer(GravityCompat.START)
            }
            R.id.itemExport ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.reportes))
                builder.setItems(R.array.formato_reportes) { dialog, which ->
                    when(which){
                        0 -> {
                            displayDialogForReportCreation(Tipo_Reporte.PREDETERMINADO)
                        }
                        1 -> {
                            displayDialogForReportCreation(Tipo_Reporte.FILTROS_ACTUALES)
                        }
                        2 -> {
                            val nav = Intent(this@MainActivity, ReportListActivity::class.java)
                            startActivity(nav)
                        }
                    }
                }

                val dialog = builder.create()
                dialog.show()

            }
            R.id.itemSort ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.ordenar))
                builder.setItems(R.array.ordenes) { dialog, which ->
                    with(sharedPref.edit()){
                        when(which){
                            0 -> {
                                putInt("ShotsOrder",Ordenes.ANTIGUEDAD_ASC)
                            }
                            1 -> {
                                putInt("ShotsOrder",Ordenes.ANTIGUEDAD_DESC)
                            }
                            else -> putInt("ShotsOrder",Ordenes.PREDETERMINADO)
                        }
                        apply()
                        updateDisplayedShots()
                    }
                }

                val dialog = builder.create()
                dialog.show()
            }
            R.id.itemFilter ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.filtrar_tomas))
                builder.setItems(R.array.filtros, DialogInterface.OnClickListener { dialog, which ->
                    sharedPref.edit().let {

                        when (which) {
                            0 -> {
                                it.putInt("ShotFilter", Filtros.PREDETERMINADO)
                                it.apply()
                                updateDisplayedShots()
                            }
                            1 -> {
                                AlertDialog.Builder(this).apply {
                                    setTitle(getString(R.string.por_evaluacion))
                                    setItems(R.array.presion_arterial) { dialog, which ->
                                        when(which){
                                            0 -> it.putInt("ShotFilter", Valoracion.HIPOTENSION)
                                            1 -> it.putInt("ShotFilter", Valoracion.NORMAL)
                                            2 -> it.putInt("ShotFilter", Valoracion.PREHIPERTENSION)
                                            3 -> it.putInt("ShotFilter", Valoracion.HIPERTENSION_1)
                                            4 -> it.putInt("ShotFilter", Valoracion.HIPERTENSION_2)
                                            5 -> it.putInt("ShotFilter", Valoracion.CRISIS)
                                        }
                                        it.apply()
                                        updateDisplayedShots()
                                    }
                                }
                                    .create().show()
                            }
                            2 -> {
                                AlertDialog.Builder(this).apply {
                                    setTitle(getString(R.string.por_momento_dia))
                                    setItems(R.array.momento) { dialog, which ->
                                        when(which){
                                            0 -> it.putInt("ShotFilter", Momento.ALEATORIO)
                                            1 -> it.putInt("ShotFilter", Momento.DESPUES_DESPERTAR)
                                            2 -> it.putInt("ShotFilter", Momento.ANTES_DORMIR)
                                            3 -> it.putInt("ShotFilter", Momento.ANTES_MEDICAMENTO)
                                            4 -> it.putInt("ShotFilter", Momento.DESPUES_MEDICAMENTO)
                                            5 -> it.putInt("ShotFilter", Momento.ANTES_COMER)
                                            6 -> it.putInt("ShotFilter", Momento.DESPUES_COMER)
                                        }
                                        it.apply()
                                        updateDisplayedShots()
                                    }
                                }.create().show()
                            }
                            3 -> {
                                AlertDialog.Builder(this).apply {
                                    setTitle(getString(R.string.por_extremidad))
                                    setItems(R.array.extremidades) { dialog, which ->
                                        when(which){
                                            0 -> it.putInt("ShotFilter", Extremidad.BRAZO_IZQUIERDO)
                                            1 -> it.putInt("ShotFilter", Extremidad.BRAZO_DERECHO)
                                            2 -> it.putInt("ShotFilter", Extremidad.MUNECA_IZQUIERDA)
                                            3 -> it.putInt("ShotFilter", Extremidad.MUNECA_DERECHA)
                                            4 -> it.putInt("ShotFilter", Extremidad.TOBILLO_IZQUIERDO)
                                            5 -> it.putInt("ShotFilter", Extremidad.TOBILLO_DERECHO)
                                            6 -> it.putInt("ShotFilter", Extremidad.MUSLO_IZQUIERDO)
                                            7 -> it.putInt("ShotFilter", Extremidad.MUSLO_DERECHO)
                                        }
                                        it.apply()
                                        updateDisplayedShots()
                                    }
                                }.create().show()
                            }

                            4 -> {
                                AlertDialog.Builder(this).apply {
                                    setTitle(getString(R.string.por_posicion))
                                    setItems(R.array.posiciones) { dialog, which ->
                                        when(which){
                                            0 -> it.putInt("ShotFilter", Posicion.SENTADO)
                                            1 -> it.putInt("ShotFilter", Posicion.ACOSTADO)
                                            2 -> it.putInt("ShotFilter", Posicion.RECOSTADO)
                                            3 -> it.putInt("ShotFilter", Posicion.DE_PIE)
                                        }
                                        it.apply()
                                        updateDisplayedShots()
                                    }
                                }.create().show()
                            }
                            else ->{
                                it.putInt("ShotFilter", Filtros.PREDETERMINADO)
                                it.apply()
                                updateDisplayedShots()
                            }
                        }

                    }
                })

                val dialog = builder.create()
                dialog.show()

            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ELIMINAR_USUARIO){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this,getString(R.string.usuario_eliminado_correctamente), Toast.LENGTH_SHORT).show()
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                with(sharedPref.edit()){
                    putInt("actualUserID", SIN_USUARIO_ACTIVO)
                    apply()

                }

                val nav = Intent(this, UserListActivity::class.java)
                startActivity(nav)
            }
        }
    }

    private fun updateDisplayedShots(){
        val shotsFragment = adapter.getItem(0) as ShotsFragment
        shotsFragment.displaySortedShotList()
        val statsFragment = adapter.getItem(1) as StatsFragment
        statsFragment.setSortedShotListForChart()
    }


    private fun createReportName(): String{
        val timeStamp: String = SimpleDateFormat("yyyy_MM_dd_HHmmss").format(Date())
        val reportName = getString(R.string.reporte) + timeStamp
        return reportName
    }

    private fun displayDialogForReportCreation(reportType : Int){

        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle(getString(R.string.crear_nuev_reporte))
        val inflater = layoutInflater
        val layoutView = inflater.inflate(R.layout.report_builder_dialog_content,null)
        builder.setView(layoutView)
        val fileName = layoutView.findViewById<EditText>(R.id.nombreReporteET)
        fileName.setText(createReportName())
        builder.setPositiveButton(getString(R.string.crear)){ _, _ ->
            val notificationManager = NotificationsManager(application)
            when(reportType){
                Tipo_Reporte.PREDETERMINADO ->{
                    notificationManager.sendNotificationForReportCreation(getString(R.string.reporte_de_presion_arterial),"",fileName.text.toString(),Tipo_Reporte.PREDETERMINADO)
                }
                Tipo_Reporte.FILTROS_ACTUALES ->{
                    notificationManager.sendNotificationForReportCreation(getString(R.string.reporte_de_presion_arterial),"",fileName.text.toString(),Tipo_Reporte.FILTROS_ACTUALES)
                }
            }

        }
        builder.setNegativeButton(getString(R.string.cancelar)){ _, _ ->
        }

        val dialog = builder.create()
        dialog.show()
    }




}
