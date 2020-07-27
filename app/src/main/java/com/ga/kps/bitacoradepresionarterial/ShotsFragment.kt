package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener
import com.mopub.mobileads.MoPubView
import helpers.Filtros
import helpers.Ordenes
import room.components.viewmodels.TomaViewModel


class ShotsFragment : Fragment(){
    lateinit var RV : RecyclerView
    lateinit var tomasViewModel : TomaViewModel
    lateinit var mAdView : MoPubView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_shots,container, false)
        RV = v.findViewById(R.id.RecViewTomas)
        RV.setHasFixedSize(true)


        val sdkConfiguration = SdkConfiguration.Builder("b88f5354d5e041d18d8e3d0ffe4c6c60")
            .withLegitimateInterestAllowed(false)
            .build()

        MoPub.initializeSdk(requireContext(),sdkConfiguration,initSdkListener())


        mAdView = v.findViewById<MoPubView>(R.id.adView)
        mAdView.setAdUnitId("b88f5354d5e041d18d8e3d0ffe4c6c60")
        return v
    }

    private fun initSdkListener(): SdkInitializationListener? {
        return SdkInitializationListener { /* MoPub SDK initialized.
               Check if you should show the consent dialog here, and make your ad requests. */

            Log.d("MoPub", "Mopub Inicializado")
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val usuarioID = sharedPref.getInt("actualUserID", -1)
        val order = sharedPref.getInt("ShotsOrder",Ordenes.PREDETERMINADO)
        val filter = sharedPref.getInt("ShotFilter", Filtros.PREDETERMINADO)
        val mLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            true
        )
        mLayoutManager.stackFromEnd = true
        RV.layoutManager = mLayoutManager

        val adapter = ShotsAdapter(context)
        tomasViewModel = ViewModelProvider(this).get(TomaViewModel::class.java)


        tomasViewModel.getFilteredShotList(usuarioID,filter,order).observe(this, Observer {

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
       // Toast.makeText(context,"Deberia ordenar: " + order + " FIltrar: " + filter, Toast.LENGTH_SHORT).show()
        tomasViewModel.getFilteredShotList(usuarioID,filter,order).observe(this, Observer {

            adapter.submitList(it)

         //   Log.d("DetallesLista", it.toString())
        })
       // RV.layoutManager?.scrollToPosition(0)
    }


}