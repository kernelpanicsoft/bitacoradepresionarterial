package helpers

import android.content.Context
import com.ga.kps.bitacoradepresionarterial.R

class ShotEvaluatorHelper (val context: Context) {
    private val limbs : Array<String> = context.resources.getStringArray(R.array.extremidades)
    private val positions : Array<String>  = context.resources.getStringArray(R.array.posiciones)
    private val moment : Array<String> = context.resources.getStringArray(R.array.momento)

    fun getLimbString(code: Int) : String{
        return when(code){
            3000 -> limbs[0]
            3001 -> limbs[1]
            3002 -> limbs[2]
            3003 -> limbs[3]
            3004 -> limbs[4]
            3005 -> limbs[5]
            3006 -> limbs[6]
            3007 -> limbs[7]
            else -> limbs[0]
        }
    }

    fun getPositionString(code: Int) : String{
        return when(code){
            4000 -> positions[0]
            4001 -> positions[1]
            4002 -> positions[2]
            4003 -> positions[3]
            else -> positions[0]
        }
    }

    fun getMomentString(code: Int) : String{
        return when(code){
            2000 -> moment[0]
            2001 -> moment[1]
            2002 -> moment[2]
            2003 -> moment[3]
            2004 -> moment[4]
            2005 -> moment[5]
            2006 -> moment[6]
            else -> moment[0]
        }
    }



}