package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import java.lang.Exception

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings,rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val privacyPolicyPreference = findPreference<PreferenceScreen>("policitica_de_privacidad")
        privacyPolicyPreference?.setOnPreferenceClickListener {

            val uris = Uri.parse("https://kernelpanicsoft.wordpress.com/2018/09/30/politica-de-privacidad/")
            val intent = Intent(Intent.ACTION_VIEW, uris)
            val b = Bundle()
            b.putBoolean("new_window",true)
            intent.putExtras(b)
            context?.startActivity(intent)
            false
        }

        val versionPreference = findPreference<PreferenceScreen>("version_app")
        try {
            val pInfo = context?.packageManager?.getPackageInfo(activity?.packageName,0)
            val version = pInfo?.versionName
            versionPreference?.summary = version
        }catch (e: Exception){

        }

        val rateUSPreference = findPreference<PreferenceScreen>("calificar_play_store")
        rateUSPreference?.setOnPreferenceClickListener {
            val uris = Uri.parse("https://play.google.com/store/apps/details?id=com.ga.kps.debitum")
            val intent = Intent(Intent.ACTION_VIEW, uris)
            val b = Bundle()
            b.putBoolean("new_window",true)
            intent.putExtras(b)
            context?.startActivity(intent)
            false
        }
    }
}