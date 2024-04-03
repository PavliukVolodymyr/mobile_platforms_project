package com.example.project

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
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
import java.io.IOException
import java.io.OutputStream

class ProfileActivity : AppCompatActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(Const.MY_SHARE_PREF, Context.MODE_PRIVATE)
    }
    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE_REQUEST = 71
    private val REQUEST_CAMERA_PERMISSION = 101
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

        val eName = findViewById<EditText>(R.id.eName)
        val eLastName= findViewById<EditText>(R.id.eLastName)
        val eEmail = findViewById<EditText>(R.id.eEmail)
        val eDate = findViewById<EditText>(R.id.eDate)
        val bUpdate = findViewById<Button>(R.id.bUpdate)
        val bSelectPhoto = findViewById<Button>(R.id.bSelectPhoto)
        val bTakePhoto = findViewById<Button>(R.id.bTakePhoto)
        imageView = findViewById(R.id.imageView)

        eName.setText(sharedPreferences.getString(Const.NAME, ""))
        eLastName.setText(sharedPreferences.getString(Const.LAST_NAME, ""))
        eEmail.setText(sharedPreferences.getString(Const.EMAIL, ""))
        eDate.setText(sharedPreferences.getString(Const.DATE, ""))

        bSelectPhoto.setOnClickListener {
            selectImage()
        }
        bTakePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        bUpdate.setOnClickListener {
            val name = eName.text.toString().trim()
            val last_name = eLastName.text.toString().trim()
            val email = eEmail.text.toString().trim()
            val date = eDate.text.toString().trim()

            if (name.isEmpty()||last_name.isEmpty() || email.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPreferences.edit().apply {
                putString(Const.NAME, name)
                putString(Const.LAST_NAME, last_name)
                putString(Const.EMAIL, email)
                putString(Const.DATE, date)
                apply()
            }

            Toast.makeText(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show()
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        }
        val filePath = sharedPreferences.getString(Const.FILE_PATH, null)

        if (!filePath.isNullOrEmpty()) {
            val file = File(filePath)
            if (file.exists()) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                val scaledBitmap = scaleBitmapToImageView(bitmap)
                imageView.setImageBitmap(scaledBitmap)
            }
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
            val uri = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val scaledBitmap = scaleBitmapToImageView(bitmap)
                imageView.setImageBitmap(scaledBitmap)
                val filePath = saveImageLocally(bitmap)
                sharedPreferences.edit().apply {
                    putString(Const.FILE_PATH, filePath)
                    apply()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val scaledBitmap = scaleBitmapToImageView(imageBitmap)
            imageView.setImageBitmap(scaledBitmap)
            val filePath = saveImageLocally(imageBitmap)
            sharedPreferences.edit().apply {
                putString(Const.FILE_PATH, filePath)
                apply()
            }
        }
    }

    private fun scaleBitmapToImageView(bitmap: Bitmap): Bitmap {
        val imageViewWidth = imageView.width
        val imageViewHeight = imageView.height

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        // Масштабуємо бітмапу пропорційно висоті ImageView, використовуючи статичне значення 300dp як базову висоту
        val targetHeightPixels = dpToPixels(300f)
        val scaleFactor = targetHeightPixels.toFloat() / bitmapHeight.toFloat()

        val newWidth = (bitmapWidth * scaleFactor).toInt()
        val newHeight = (bitmapHeight * scaleFactor).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun dpToPixels(dp: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }





    private fun saveImageLocally(bitmap: Bitmap): String? {
        val directory = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "profile_images")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(directory, fileName)
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
