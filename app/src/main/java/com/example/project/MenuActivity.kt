package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
class MenuActivity :AppCompatActivity() {

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(Const.MY_SHARE_PREF, Context.MODE_PRIVATE)
    }
    lateinit var recycleView:RecyclerView
    lateinit var rvProduct:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val bLogOut= findViewById<Button>(R.id.logOut)
        val bStudentList= findViewById<Button>(R.id.bStudentList)
        val bPairList= findViewById<Button>(R.id.bPairList)
        val bProfileActivity= findViewById<Button>(R.id.bProfile)
//        val intentStudentList= Intent(this,TeacherList::class.java)
//        val intentLessonList= Intent(this,LessonList::class.java)
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
//        bStudentList.setOnClickListener{
//            startActivity(intentStudentList)
//        }
//        bPairList.setOnClickListener{
//            startActivity(intentLessonList)
//        }

    }
}