package com.ga.kps.bitacoradepresionarterial

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import helpers.Codigos_solicitud
import helpers.Genero
import helpers.Permisos_solicitados
import kotlinx.android.synthetic.main.activity_add_user.*
import model.Usuario
import room.components.viewmodels.UsuarioViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddUserActivity : AppCompatActivity() {
    lateinit var usuarioViewModel: UsuarioViewModel
    private val calendario: Calendar = Calendar.getInstance()
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    private val sdfDisplayDate = SimpleDateFormat.getDateInstance()
    private var mCurrentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.registrar_usuario)

        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)

        fechaNacimientoBT.setOnClickListener {
            val datePickerFragment = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendario.set(Calendar.YEAR, year)
                calendario.set(Calendar.MONTH, month)
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                //Toast.makeText(this@AnadirCitaMedicaActivity,"Fecha seleccionada: " + sdf.format(calendario.time), Toast.LENGTH_SHORT).show()

                fechaNacimientoBT.text =  sdfDisplayDate.format(calendario.time)
            }, calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH))
            datePickerFragment.show()
        }

        addUserFAB.setOnClickListener {

            if(nombreET.text.isNullOrEmpty() || apellidosET.text.isNullOrEmpty()){
                Snackbar.make(it,getString(R.string.es_necesario_especificar_nombre_apellidos), Snackbar.LENGTH_LONG).show()
            }else{
                val newUser = Usuario(0)
                newUser.imagen_perfil = mCurrentPhotoPath
                newUser.nombre = nombreET.text.toString()
                newUser.apellidos = apellidosET.text.toString()
                newUser.fecha_nacimiento = sdf.format(calendario.time)

                when(findViewById<RadioButton>(generoRG.checkedRadioButtonId)){
                    masculinoRB ->{
                        newUser.genero = Genero.MASCULINO
                    }
                    femeninoRB ->{
                        newUser.genero = Genero.FEMENINO
                    }
                }

                saveUserToDB(newUser)
                finish()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_profile_pic,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                onBackPressed()
                return true 
            }
            R.id.item_add_photo -> {
                val builder = AlertDialog.Builder(this@AddUserActivity)
                builder.setTitle(getString(R.string.elegir_imagen_perfil_desde))
                builder.setItems(R.array.fuente_imagen){ dialog, which ->
                    when(which){
                        // pick from gallery
                        0 -> {

                        }
                        // take a photo
                        1 -> {
                            dispatchTakePictureIntent()
                        }
                    }
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveUserToDB(usuario: Usuario){
        usuarioViewModel.insert(usuario)
        setResult(Activity.RESULT_OK)
    }


    @Throws(IOException::class)
    private fun createImageFile() : File {

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            mCurrentPhotoPath = absolutePath
        }

    }

    @Throws(IOException::class)
    private fun deleteImageFile(){
        val photoFile = File(mCurrentPhotoPath)
        val photoUri: Uri = FileProvider.getUriForFile(
            this,
            "com.kps.spart.android.fileprovider",
            photoFile
        )

        this.contentResolver.delete(photoUri,null,null)
    }

    @Throws(IOException::class)
    private fun compressImage(scaledBitmap : Bitmap){
        val photoFile = File(mCurrentPhotoPath)
        Log.d("Resoluacion","Propiedades: " + scaledBitmap.height + " | " + scaledBitmap.width)
        val out = FileOutputStream(photoFile)
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG,85,out)
        out.close()
    }

    fun rotateImage(source : Bitmap, angle : Float) : Bitmap{
        val matrix = Matrix()
        matrix.postRotate(angle)

        return Bitmap.createBitmap(source,0,0,source.width,source.height,matrix,true)
    }

    private fun determinatePicOrientation(bmOptions : BitmapFactory.Options){
        val exif = ExifInterface(mCurrentPhotoPath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED)
        if(!mCurrentPhotoPath.isBlank()){
            BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions)?.also { bitmap ->
                var rotatedBitmap : Bitmap? = null
                when(orientation){
                    ExifInterface.ORIENTATION_ROTATE_90 -> {
                        rotatedBitmap = rotateImage(bitmap,90f)
                    }
                    ExifInterface.ORIENTATION_ROTATE_180 -> {
                        rotatedBitmap = rotateImage(bitmap,180f)
                    }
                    ExifInterface.ORIENTATION_ROTATE_270 -> {
                        rotatedBitmap = rotateImage(bitmap, 270f)
                    }
                    ExifInterface.ORIENTATION_NORMAL -> {
                        rotatedBitmap = bitmap
                    }
                    else -> {
                        rotatedBitmap = bitmap
                    }
                }
                compressImage(rotatedBitmap)
            }


        }
    }

    private fun dispatchTakePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
               val photoFile: File? = try{
                   createImageFile()
               } catch (ex: IOException){
                   null
               }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.ga.kps.bitacoradepresionarterial",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent,Codigos_solicitud.SOLICITAR_IMAGEN_DESDE_CAMARA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Codigos_solicitud.SELECCIONAR_IMAGEN && resultCode == Activity.RESULT_OK){

        }
        if(requestCode == Codigos_solicitud.SOLICITAR_IMAGEN_DESDE_CAMARA && resultCode == Activity.RESULT_OK){
            setPic()
        }
    }


    private fun setPic(){
        val targetW: Int = resources.getDimension(R.dimen.UserProfileImageSingle).toInt()
        val targetH: Int = resources.getDimension(R.dimen.UserProfileImageSingle).toInt()


        val bmOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            inJustDecodeBounds = false
            Log.d("ScaleFator: ", scaleFactor.toString() + " | " + photoW + " | " + targetW + " | " + photoH + " | " + targetH)
            inSampleSize = scaleFactor
            inPurgeable = true
        }

        determinatePicOrientation(bmOptions)



        BitmapFactory.decodeFile(mCurrentPhotoPath)?.also {  bitmap ->
            profileIV.setImageBitmap(bitmap)
        }
    }
}
