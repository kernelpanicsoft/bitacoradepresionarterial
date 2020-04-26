package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import helpers.Filtros
import helpers.Ordenes
import room.components.viewmodels.TomaViewModel

class ShotsFragment : Fragment(){
    lateinit var RV : RecyclerView
    lateinit var tomasViewModel : TomaViewModel



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_shots,container, false)
        RV = v.findViewById(R.id.RecViewTomas)
        RV.setHasFixedSize(true)
        return v
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val usuarioID = sharedPref.getInt("actualUserID", -1)
        val order = sharedPref.getInt("ShotsOrder",0)

        val mLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            true
        )
        mLayoutManager.stackFromEnd = true
        RV.layoutManager = mLayoutManager

        val adapter = ShotsAdapter(context)
        tomasViewModel = ViewModelProvider(this).get(TomaViewModel::class.java)

        tomasViewModel.getSortedShotList(usuarioID,order).observe(this, Observer {
            adapter.submitList(it)
        })

        RV.adapter = adapter

        adapter.setOnClickListener(View.OnClickListener {
            val nav = Intent(context,ShotDetailActivity::class.java)
            val selectedShot = adapter.getShotAt(RV.getChildAdapterPosition(it))
            nav.putExtra("SHOT_ID", selectedShot.id)
            startActivity(nav)
        })
    }

    fun displaySortedShotList(){
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val usuarioID = sharedPref.getInt("actualUserID", -1)
        val filter = sharedPref.getInt("ShotFilter", Filtros.PREDETERMINADO)
        val order= sharedPref.getInt("ShotsOrder",Ordenes.PREDETERMINADO)
        val adapter = ShotsAdapter(context)

        tomasViewModel.getFilteredShotList(usuarioID,filter,order).observe(this, Observer {
            adapter.submitList(it)
            Log.d("ListaFiltrada: ",it.size.toString() + "# " +it.toString())
        })
    }

    fun displayFilteredShotList(filter: Int, subFilter: Int, order: Int){
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val usuarioID = sharedPref.getInt("actualUserID", -1)
        val adapter = ShotsAdapter(context)
/*
        tomasViewModel.getFilteredShotList(usuarioID,filter,subFilter, order).observe(this, Observer {
            adapter.submitList(it)

        })
        */

    }


}