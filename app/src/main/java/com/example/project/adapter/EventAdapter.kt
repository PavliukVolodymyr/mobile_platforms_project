package com.example.project.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.EventModel

class EventAdapter(private val eventList: List<EventModel>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event_layout, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.tvName.text = event.name
        holder.itemView.setOnClickListener {
            showEventDetailsDialog(holder.itemView.context, event.description)
        }
    }

    private fun showEventDetailsDialog(context: Context, description: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Additional Information")
        builder.setMessage(description)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}
