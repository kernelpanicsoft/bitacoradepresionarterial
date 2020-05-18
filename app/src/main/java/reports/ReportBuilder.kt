package reports

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.os.Environment
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import java.io.File

class ReportBuilder(private val applicationContext: Context) {

    lateinit var root: File
    lateinit var assetManager: AssetManager
    lateinit var pageImage: Bitmap


    private fun setup(){

        PDFBoxResourceLoader.init(applicationContext)
        root = applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!
        assetManager = applicationContext.assets

    }
}