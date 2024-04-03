package com.example.project

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project.adapter.CityAdapter
import com.example.project.model.CityModel

class CityActivity : AppCompatActivity() {

    lateinit var adapter: CityAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)

        val addButton = findViewById<Button>(R.id.btnAddCity)
        recyclerView = findViewById(R.id.recyclerViewCity)
        initialize()

        addButton.setOnClickListener {
            showAddCityDialog()
        }
    }

    private fun showAddCityDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.addCity))

        val view = layoutInflater.inflate(R.layout.dialog_add_city, null)
        builder.setView(view)

        val etCity = view.findViewById<EditText>(R.id.etCity)
        val etCountry = view.findViewById<EditText>(R.id.etCountry)

        builder.setPositiveButton(getString(R.string.add)) { dialog, _ ->
            val city = etCity.text.toString()
            val country = etCountry.text.toString()
            if (city.isNotEmpty() && country.isNotEmpty()) {
                val newCity = CityModel(city, country)
                adapter.addCity(newCity)
            } else {
                Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun initialize() {
        recyclerView = findViewById(R.id.recyclerViewCity)
        adapter = CityAdapter()
        recyclerView.adapter = adapter
        adapter.setList(generateCityList())
    }

    private fun generateCityList(): List<CityModel> {
        val cityList = ArrayList<CityModel>()
        cityList.add(CityModel("Kyiv", "Ukraine"))
        cityList.add(CityModel("New York", "USA"))
        cityList.add(CityModel("London", "UK"))
        cityList.add(CityModel("Paris", "France"))
        cityList.add(CityModel("Berlin", "Germany"))
        cityList.add(CityModel("Tokyo", "Japan"))
        cityList.add(CityModel("Moscow", "Russia"))
        cityList.add(CityModel("Sydney", "Australia"))
        cityList.add(CityModel("Rome", "Italy"))
        cityList.add(CityModel("Toronto", "Canada"))
        // Add more cities as needed
        return cityList
    }
}
