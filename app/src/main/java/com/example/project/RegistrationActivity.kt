package com.example.project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class RegistrationActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: ImageView
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(Const.MY_SHARE_PREF, Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val eName= findViewById<EditText>(R.id.eName)
        val eLastName= findViewById<EditText>(R.id.eLastName)
        val eLogin= findViewById<EditText>(R.id.eLogin)
        val ePass= findViewById<EditText>(R.id.ePassword)
        val eDate= findViewById<EditText>(R.id.eDate)
        val ePhone= findViewById<EditText>(R.id.eEmail)
        val bReg= findViewById<Button>(R.id.bReg)
        val eConfirm= findViewById<EditText>(R.id.eConfirm)
        val intentAuth= Intent(this,AuthActivity::class.java)
//        imageView = findViewById(R.id.img)
//        imageView.setOnClickListener {
//            // Запускаємо Intent для вибору зображення з галереї
//            val galleryIntent = Intent(
//                Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            )
//            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
//        }
        bReg.setOnClickListener {
            val name = eName.text.toString().trim()
            val last_name = eLastName.text.toString().trim()
            val login = eLogin.text.toString().trim()
            val email = ePhone.text.toString().trim()
            val date = eDate.text.toString().trim()
            val pass = ePass.text.toString().trim()
            val conf = eConfirm.text.toString().trim()

            if (name.isEmpty() || last_name.isEmpty() || login.isEmpty() || email.isEmpty() || date.isEmpty() || pass.isEmpty() || conf.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != conf) {
                Toast.makeText(this, getString(R.string.passwordErr), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPreferences.edit().apply {
                putString(Const.NAME, name)
                putString(Const.LAST_NAME, last_name)
                putString(Const.LOGIN, login)
                putString(Const.EMAIL, email)
                putString(Const.DATE, date)
                putString(Const.PASSWORD, pass)
                putInt(Const.AUTH,0)
                apply()
            }
            startActivity(intentAuth)
        }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//            val selectedImageUri = data.data
//            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
//            imageView.setImageBitmap(bitmap)
//            sharedPreferences.edit().apply {
//                putString("url", selectedImageUri.toString())
//                apply()
//            }
//        }
//    }
}