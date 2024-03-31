package com.example.project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.project.Const
import com.example.project.MenuActivity
import com.example.project.R

class AuthActivity : AppCompatActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(Const.MY_SHARE_PREF, Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val eLogin=findViewById<EditText>(R.id.eLogin)
        val ePass=findViewById<EditText>(R.id.ePassword)
        val intentReg = Intent(this, RegistrationActivity::class.java)
        val intentMenu = Intent(this, MenuActivity::class.java)
        val bReg= findViewById<Button>(R.id.bReg)
        val bLog= findViewById<Button>(R.id.bAuth)
        bReg.setOnClickListener {
            startActivity(intentReg)
        }
        val isAuth = sharedPreferences.getInt(Const.AUTH, 0)
        if (isAuth!=0){
            startActivity(intentMenu)
        }
        bLog.setOnClickListener {
            val login = eLogin.text.toString().trim()
            val pass = ePass.text.toString().trim()

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedLogin = sharedPreferences.getString(Const.LOGIN, "")
            val savedPass = sharedPreferences.getString(Const.PASSWORD, "")

            if (login == savedLogin && pass == savedPass && savedPass != "" && savedLogin != "") {
                sharedPreferences.edit().putInt(Const.AUTH,1).apply()
                startActivity(intentMenu)
            } else {
                Toast.makeText(this, getString(R.string.incorrect), Toast.LENGTH_SHORT).show()
            }
        }

    }
}