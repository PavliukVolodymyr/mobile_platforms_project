package com.example.project

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ProfileActivity : AppCompatActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(Const.MY_SHARE_PREF, Context.MODE_PRIVATE)
    }
    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE_REQUEST = 71
    private val REQUEST_CAMERA_PERMISSION = 101
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

        val eName = findViewById<EditText>(R.id.eName)
        val ePhone = findViewById<EditText>(R.id.ePhone)
        val eDate = findViewById<EditText>(R.id.eDate)
        val bUpdate = findViewById<Button>(R.id.bUpdate)
        val bSelectPhoto = findViewById<Button>(R.id.bSelectPhoto)
        val bTakePhoto = findViewById<Button>(R.id.bTakePhoto)
        imageView = findViewById(R.id.imageView)

        eName.setText(sharedPreferences.getString(Const.NAME, ""))
        ePhone.setText(sharedPreferences.getString(Const.PHONE, ""))
        eDate.setText(sharedPreferences.getString(Const.DATE, ""))

        bSelectPhoto.setOnClickListener {
            selectImage()
        }
        bTakePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        bUpdate.setOnClickListener {
            val name = eName.text.toString().trim()
            val phone = ePhone.text.toString().trim()
            val date = eDate.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPreferences.edit().apply {
                putString(Const.NAME, name)
                putString(Const.PHONE, phone)
                putString(Const.DATE, date)
                apply()
            }

            Toast.makeText(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val scaledBitmap = scaleBitmapToImageView(bitmap)
                imageView.setImageBitmap(scaledBitmap)
                saveImageToGallery(scaledBitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val scaledBitmap = scaleBitmapToImageView(imageBitmap)
            imageView.setImageBitmap(scaledBitmap)
            saveImageToGallery(scaledBitmap)
        }
    }

    private fun scaleBitmapToImageView(bitmap: Bitmap): Bitmap {
        val imageViewWidth = imageView.width
        val imageViewHeight = imageView.height

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        val scaleFactor = if (bitmapWidth > bitmapHeight) {
            imageViewWidth.toFloat() / bitmapWidth.toFloat()
        } else {
            imageViewHeight.toFloat() / bitmapHeight.toFloat()
        }

        val newWidth = (bitmapWidth * scaleFactor).toInt()
        val newHeight = (bitmapHeight * scaleFactor).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val imagesCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }

        val contentResolver = applicationContext.contentResolver
        val uri = contentResolver.insert(imagesCollection, contentValues)

        uri?.let {
            contentResolver.openOutputStream(it).use { outputStream ->
                outputStream?.let { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            } else {
                MediaScannerConnection.scanFile(applicationContext, arrayOf(uri.path), arrayOf("image/jpeg"), null)
            }
        }
    }
}
