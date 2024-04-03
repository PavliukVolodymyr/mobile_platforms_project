package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.SharedPreferences
import android.widget.TextView

class MenuActivity :AppCompatActivity() {

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(Const.MY_SHARE_PREF, Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val bLogOut= findViewById<Button>(R.id.logOut)
        val bEventList= findViewById<Button>(R.id.bEventList)
        val bCityList= findViewById<Button>(R.id.bCityList)
        val bProfileActivity= findViewById<Button>(R.id.bProfile)
        val intentEventList= Intent(this,EventActivity::class.java)
        val intentCityList= Intent(this,CityActivity::class.java)
        val intentAuth = Intent(this,AuthActivity::class.java)
        val intentProfile = Intent(this,ProfileActivity::class.java)
        bLogOut.setOnClickListener {
            sharedPreferences.edit().putInt(Const.AUTH,0).apply()
            intentAuth.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentAuth)
            finish()
        }
        bProfileActivity.setOnClickListener{
            startActivity(intentProfile)
        }
        bCityList.setOnClickListener{
            startActivity(intentCityList)
        }
        bEventList.setOnClickListener{
            startActivity(intentEventList)
        }
        // Отримання даних користувача з SharedPreferences
        val userName = sharedPreferences.getString(Const.NAME, "")
        val userLastName = sharedPreferences.getString(Const.LAST_NAME, "")
        val userEmail = sharedPreferences.getString(Const.EMAIL, "")
        val userInfo = "$userName $userLastName\n$userEmail"

        // Знаходження TextView і встановлення тексту
        val tvUserInfo = findViewById<TextView>(R.id.tvUserInfo)
        tvUserInfo.text = userInfo

    }

    override fun onResume() {
        super.onResume()
        // Отримання даних користувача з SharedPreferences
        val userName = sharedPreferences.getString(Const.NAME, "")
        val userLastName = sharedPreferences.getString(Const.LAST_NAME, "")
        val userEmail = sharedPreferences.getString(Const.EMAIL, "")
        val userInfo = "$userName $userLastName\n$userEmail"

        // Знаходження TextView і встановлення тексту
        val tvUserInfo = findViewById<TextView>(R.id.tvUserInfo)
        tvUserInfo.text = userInfo
    }
}