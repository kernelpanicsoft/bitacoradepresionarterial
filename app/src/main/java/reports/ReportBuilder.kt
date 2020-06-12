package reports

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.ga.kps.bitacoradepresionarterial.R
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.font.PDFont
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import helpers.Filtros
import helpers.Ordenes
import helpers.ShotEvaluatorHelper
import model.Reporte
import model.Toma
import room.components.repositories.TomaRepository
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.app.Application
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.Usuario
import room.components.repositories.ReporteRepository
import room.components.repositories.UsuarioRepository

class ReportBuilder(private val application: Application) {

    lateinit var root: File
    lateinit var assetManager: AssetManager
    lateinit var pageImage: Bitmap


    fun setup(){

        PDFBoxResourceLoader.init(application)
        root = application.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!
        assetManager = application.assets

    }

    fun createPDF(reportName: String){
        Log.d("ReportBuilder","Estas llamando a crear reporte 2")

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(application)
        val usuarioID = sharedPref.getInt("actualUserID", -1)
        val order = sharedPref.getInt("ShotsOrder", Ordenes.PREDETERMINADO)
        val filter = sharedPref.getInt("ShotFilter", Filtros.PREDETERMINADO)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        val sdfReportDate = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
        val sdfReportTime = SimpleDateFormat.getTimeInstance(DateFormat.SHORT)

        val shotEvaluatorHelper: ShotEvaluatorHelper = ShotEvaluatorHelper(application)

        val document = PDDocument()
        val page = PDPage()
        document.addPage(page)

        val font : PDFont = PDType1Font.HELVETICA

        var contentStream: PDPageContentStream

        try{

            Thread(Runnable {
                val usuario = getUserForReport(usuarioID)
                contentStream = PDPageContentStream(document, page)
                contentStream.beginText()
                contentStream.setNonStrokingColor(0,0,0)
                contentStream.setFont(font,12f)
                contentStream.newLineAtOffset(30f,750f)
                contentStream.showText(application.getString(R.string.reporte_de_presion_arterial))
                contentStream.newLineAtOffset(0f, -15f)
                contentStream.showText(usuario.nombre + " " + usuario.apellidos)
                contentStream.newLineAtOffset(0f, -15f)

                val calendarAux = Calendar.getInstance()
                calendarAux.time = sdf.parse(usuario.fecha_nacimiento)
                contentStream.showText(application.getString(R.string.fecha_nacimiento_reporte) + " " + sdfReportDate.format(calendarAux.time))
                contentStream.newLineAtOffset(0f, -25f)


                contentStream.showText(application.getString(R.string.fecha))
                contentStream.newLineAtOffset(150f, 0f)
                contentStream.showText(application.getString(R.string.sistolica))
                contentStream.newLineAtOffset(70f, 0f)
                contentStream.showText(application.getString(R.string.diastolica))
                contentStream.newLineAtOffset(70f, 0f)
                contentStream.showText(application.getString(R.string.pulso))
                contentStream.newLineAtOffset(70f, 0f)
                contentStream.showText(application.getString(R.string.momento_dia))

                var listaTomas :  List<Toma>
                contentStream.newLineAtOffset(-360f, -20f)

                listaTomas = getListForDefaultReport(usuarioID)
                var date: Date? = null
                var calendar = Calendar.getInstance()
                for((index, toma) in listaTomas.withIndex()){
                    date = sdf.parse(toma.fecha_hora!!)
                    calendar.time = date

                    contentStream.showText(sdfReportDate.format(calendar.time) + " " + sdfReportTime.format(calendar.time))
                    contentStream.newLineAtOffset(150f, 0f)
                    contentStream.showText(toma.sistolica.toString())
                    contentStream.newLineAtOffset(70f, 0f)
                    contentStream.showText(toma.diastolica.toString())
                    contentStream.newLineAtOffset(70f, 0f)
                    contentStream.showText(toma.pulso.toString())
                    contentStream.newLineAtOffset(70f, 0f)
                    contentStream.showText(shotEvaluatorHelper.getMomentString(toma.momento!!))
                    contentStream.newLineAtOffset(-360f, -25f)

                    if(index > 1 && index % 26 == 0){
                        contentStream.close()
                        val newPage = PDPage()
                        document.addPage(newPage)
                        contentStream = PDPageContentStream(document, newPage)
                        contentStream.beginText()
                        contentStream.setNonStrokingColor(0,0,0)
                        contentStream.setFont(font,12f)
                        contentStream.newLineAtOffset(30f,750f)


                    }
                }

                contentStream.close()


                val path = root.absolutePath + "/"+ reportName + ".pdf"
                document.save(path)
                document.close()

                val reporte = Reporte(0)
                reporte.public_name = reportName
                reporte.file = path

                reporte.usuario_id = usuarioID
                insertReportOnDB(reporte)
            }).start()

        } catch (e: IOException){
            Toast.makeText(application, application.getString(R.string.ocurrio_un_error_crear_reporte), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getListForDefaultReport(id: Int) : List<Toma>{
        val tomasRepository : TomaRepository = TomaRepository(application)
        return tomasRepository.getTomasUsuarioReporteAsc(id)
    }

    private fun insertReportOnDB(report: Reporte){
        GlobalScope.launch {
            val reporteRepository = ReporteRepository(application)
            reporteRepository.insert(report)
        }
    }

    private fun getUserForReport(id: Int) : Usuario{
        val usuarioRepository = UsuarioRepository(application)
        return usuarioRepository.getUsuarioForReport(id)
    }

}