package helpers

import android.content.Context
import androidx.core.content.ContextCompat
import com.ga.kps.bitacoradepresionarterial.R

class BloodPressureEvaluatorHelper (val context: Context) {
    private val bloodPressureCategories : Array<String>  = context.resources.getStringArray(R.array.presion_arterial)

    fun getBloodPressureEvaluation(sys: Int, dia: Int) : String{
        return if(sys < 90 || dia < 60){
            bloodPressureCategories[0]
        }else if(sys <= 120 && dia <= 80){
            bloodPressureCategories[1]
        }else if(sys <= 129 && dia <= 80){
            bloodPressureCategories[2]
        }else if(sys <= 139 || dia <= 89){
            bloodPressureCategories[3]
        }else if(sys >= 140 || dia >= 90){
            bloodPressureCategories[4]
        }else if(sys >= 180 || dia >= 120){
            bloodPressureCategories[5]
        }else{
            bloodPressureCategories[0]
        }
    }

    fun getBloodPressureColor(valuation : String) : Int{
        return when(valuation){
            bloodPressureCategories[0] -> ContextCompat.getColor(context,R.color.hipotension)
            bloodPressureCategories[1] -> ContextCompat.getColor(context,R.color.normalBP)
            bloodPressureCategories[2] -> ContextCompat.getColor(context,R.color.prehipertensionBP)
            bloodPressureCategories[3] -> ContextCompat.getColor(context,R.color.hipertension1)
            bloodPressureCategories[4] -> ContextCompat.getColor(context,R.color.hipertension2)
            bloodPressureCategories[5] -> ContextCompat.getColor(context,R.color.hipertensioncrysis)
            else -> ContextCompat.getColor(context,R.color.hipotension)
        }

    }
}