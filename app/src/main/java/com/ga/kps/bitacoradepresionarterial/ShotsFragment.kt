package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val mLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            true
        )
        mLayoutManager.stackFromEnd = true
        RV.layoutManager = mLayoutManager

        val adapter = ShotsAdapter(context)
        tomasViewModel = ViewModelProviders.of(this).get(TomaViewModel::class.java)
        tomasViewModel.getTomasUsuario(usuarioID).observe(this, Observer {
            adapter.submitList(it)
            Log.d("Lista", it.toString())
        })

        RV.adapter = adapter

        adapter.setOnClickListener(View.OnClickListener {
            val nav = Intent(context,ShotDetailActivity::class.java)
            val selectedShot = adapter.getShotAt(RV.getChildAdapterPosition(it))
            nav.putExtra("SHOT_ID", selectedShot.id)
            startActivity(nav)
        })
    }
}