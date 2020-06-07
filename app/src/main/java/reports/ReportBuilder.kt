package reports

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.os.Environment
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import java.io.File

class ReportBuilder(private val context: Context) {

    lateinit var root: File
    lateinit var assetManager: AssetManager
    lateinit var pageImage: Bitmap


    private fun setup(){

        PDFBoxResourceLoader.init(context)
        root = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!
        assetManager = context.assets

    }
}