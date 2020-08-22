package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import room.components.viewmodels.RecordatorioViewModel

class RemindersFragment : Fragment() {
    lateinit var RV : RecyclerView
    lateinit var recordatoriosViewModel : RecordatorioViewModel
    lateinit var mAdView : AdView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_reminder,container, false)
        RV = v.findViewById(R.id.RecViewRecordatorios)
        RV.setHasFixedSize(true)

        MobileAds.initialize(context){}
        mAdView = v.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        return v
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val usuarioID = sharedPref.getInt("actualUserID", -1)

        val mLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            true
        )
        mLayoutManager.stackFromEnd = true
        RV.layoutManager = mLayoutManager

        val adapter = RemindersAdapter(context)
        recordatoriosViewModel = ViewModelProvider(this).get(RecordatorioViewModel::class.java)
        recordatoriosViewModel.getRecordatoriosUsuario(usuarioID).observe(this, Observer {
            adapter.submitList(it)
            Log.d("Usuario actual", usuarioID.toString())
            Log.d("ListaRecordatorios", it.toString())
        })

        RV.adapter = adapter

        adapter.setOnClickListener(View.OnClickListener {
            val nav = Intent(context, ReminderDetailActivity::class.java)
            val selectedReminder = adapter.getReminderAt(RV.getChildAdapterPosition(it))
            nav.putExtra("REMINDER_ID", selectedReminder.id)
            startActivity(nav)

        })
    }
}