package com.ga.kps.bitacoradepresionarterial

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.util.Log
import model.Recordatorio
import room.components.repositories.RecordatorioRepository

class RemindersSetupIntentService : IntentService("RemindersSetupIntentService") {

    override fun onHandleIntent(p0: Intent?) {
        try {
            Log.d("Servicio", "Servicio lanzado")

                val recordatorios = getReminderList()
                for((index, recordatorio) in recordatorios.withIndex()){
                    Log.d("Recordatorio:", recordatorio.hora + " " + recordatorio.dias_de_semana)
                    for(a: Char in recordatorio.dias_de_semana){
                        Log.d("RecordatorioChar", a.toString())
                    }
                }
                Log.d("Servicio", "Estas dentro del hilo")


        } catch (e: InterruptedException){
            Thread.currentThread().interrupt()
        }
    }

    private fun getReminderList() : List<Recordatorio>{
        val recordatorioRepository = RecordatorioRepository(application)
        return recordatorioRepository.getRecordatoriosForService()
    }

    private fun setAlarmForDay(recordatorio: Recordatorio){

    }

}
