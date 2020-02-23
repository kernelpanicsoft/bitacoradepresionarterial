package helpers

import android.content.Context
import com.ga.kps.bitacoradepresionarterial.R

class ShotEvaluatorHelper (val context: Context) {
    private val limbs : Array<String> = context.resources.getStringArray(R.array.extremidades)
    private val positions : Array<String>  = context.resources.getStringArray(R.array.posiciones)
    private val moment : Array<String> = context.resources.getStringArray(R.array.momento)

    fun getLimbString(code: Int) : String{
        return limbs[code]
    }

    fun getPositionString(code: Int) : String{
        return positions[code]
    }

    fun getMomentString(code: Int) : String{
        return moment[code]
    }



}