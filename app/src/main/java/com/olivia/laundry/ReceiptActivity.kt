package com.olivia.laundry

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.olivia.laundry.databinding.ActivityReceiptBinding
import java.io.File
import java.io.FileOutputStream
import java.util.Date


class ReceiptActivity : AppCompatActivity() {
    lateinit var binding:ActivityReceiptBinding
    lateinit var sharePath:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()

        Log.d("RecieptActivity", "onCreate: ${intent.extras!!.getString("tanggalOrder")}")

        binding.textView29.text = intent.extras!!.getString("orderID")
        binding.textView30.text = intent.extras!!.getString("orderName")
        binding.textView33.text = intent.extras!!.getString("tanggalOrder")
        binding.textView42.text = intent.extras!!.getString("tanggalOrder")
        binding.textView68.text = intent.extras!!.getString("serviceSatuan")
        binding.textView26.text = intent.extras!!.getString("banyak")
        binding.textView69.text = intent.extras!!.getString("hasilPerkalian")
        binding.textView40.text = intent.extras!!.getString("total")

        binding.textView38.text = binding.textView69.text


        val getPaymentDetail = db.collection("ListPesanan").document(binding.textView29.text.toString()).collection("Payment").document("detailPayment")
        getPaymentDetail.get()
            .addOnSuccessListener {
               binding.textView36.text =  it.getString("method")
               binding.textView23.text =  it.getString("transactionID")
            }
            .addOnFailureListener {
                Log.e("ReceiptActivity", "onCreate: gagal menampilkan data", it.cause)
            }

        binding.imageButton4.setOnClickListener {
            verifyStoragePermissions(this)
            takeScreenshot()
        }

        binding.imageButton3.setOnClickListener {
            verifyStoragePermissions(this)
            val uri = getImageToShare(captureScreenshot(binding.screenshotView))
            val intent = Intent(Intent.ACTION_SEND)

            // putting uri of image to be shared

            // putting uri of image to be shared
            intent.putExtra(Intent.EXTRA_STREAM, uri)

            // adding text to share

            // adding text to share
            intent.putExtra(Intent.EXTRA_TEXT, "Transaksi Sukses")

            // Add subject Here

            // Add subject Here
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

            // setting type to image

            // setting type to image
            intent.type = "image/jpg"

            // calling startactivity() to share

            // calling startactivity() to share

            val chooser = Intent.createChooser(intent, "Share Via")

            val resInfoList = this.packageManager.queryIntentActivities(
                chooser,
                PackageManager.MATCH_DEFAULT_ONLY
            )

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            startActivity(chooser)
        }

        binding.button3.setOnClickListener {
            finish()
        }

        
    }


    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
               PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE
            )
        }
    }


    private fun captureScreenshot(view: View):Bitmap{
        val returnBitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null)bgDrawable.draw(canvas)else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnBitmap

    }

    fun takeScreenshot(){
        val now = Date()
        DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

        try {


           val mPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    this@ReceiptActivity.getExternalFilesDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()).toString() + "/" + now + ".jpeg"
            }else{
               this@ReceiptActivity.getExternalFilesDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()).toString() + "/" + now + ".jpeg"

           }
            // create bitmap screen capture
            Log.d("Path", "takeScreenshot: $mPath")
            // create bitmap screen capture
            val bitmap: Bitmap = captureScreenshot(binding.screenshotView)

            val imageFile = File(mPath)

            val outputStream = FileOutputStream(imageFile)
            val quality = 100

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()
            sharePath = mPath
            Log.d("Path", "takeScreenshot: $sharePath")
            MediaStore.Images.Media.insertImage(contentResolver,bitmap,"Screenshot $now","")
            Toast.makeText(this, "Berhasil Menyimpan Screenshot", Toast.LENGTH_SHORT).show()
        }catch (e:Throwable) {
            e.printStackTrace()
        }
    }

    private fun openScreenshot(imageFile: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val uri = Uri.fromFile(imageFile)
        intent.setDataAndType(uri, "image/*")
        startActivity(intent)
    }

    private fun getImageToShare(bitmap: Bitmap): Uri? {
        val imagefolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()
            val file = File(imagefolder, "buktipembayaran.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(this, "com.olivia.laundry.fileprovider", file)
        } catch (e: Exception) {
            Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }
}