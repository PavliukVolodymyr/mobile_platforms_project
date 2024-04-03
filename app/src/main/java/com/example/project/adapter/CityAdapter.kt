package com.example.project.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.CityModel

class CityAdapter : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private var cityList = emptyList<CityModel>()

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCity = itemView.findViewById<TextView>(R.id.tvCity)
        val tvCountry = itemView.findViewById<TextView>(R.id.tvCountry)
        val bDel: Button = itemView.findViewById(R.id.bDel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city_layout, parent, false)
        return CityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cityList[position]
        holder.tvCity.text = city.city
        holder.tvCountry.text = city.country
        holder.bDel.setOnClickListener {
            val cityToDelete = cityList[position]
            cityList = cityList.minus(cityToDelete)
            notifyDataSetChanged()
        }

        fun showEditCityDialog(city: CityModel, position: Int) {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle(holder.itemView.context.getString(R.string.editCity))

            val view = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_add_city, null)
            builder.setView(view)

            val etCity = view.findViewById<EditText>(R.id.etCity)
            val etCountry = view.findViewById<EditText>(R.id.etCountry)

            etCity.setText(city.city)
            etCountry.setText(city.country)

            builder.setPositiveButton(holder.itemView.context.getString(R.string.update)) { dialog, _ ->
                val newCity = etCity.text.toString()
                val newCountry = etCountry.text.toString()
                if (newCity.isNotEmpty() && newCountry.isNotEmpty()) {
                    val updatedCity = CityModel(newCity, newCountry)
                    updateCity(position, updatedCity)
                } else {
                    Toast.makeText(holder.itemView.context, holder.itemView.context.getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }

            builder.setNegativeButton(holder.itemView.context.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }

        holder.itemView.setOnClickListener {
            showEditCityDialog(city, position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<CityModel>) {
        cityList = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addCity(city: CityModel) {
        cityList += city
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCity(position: Int, updatedCity: CityModel) {
        val updatedList = cityList.toMutableList()
        updatedList[position] = updatedCity
        cityList = updatedList.toList()
        notifyItemChanged(position)
    }
}
