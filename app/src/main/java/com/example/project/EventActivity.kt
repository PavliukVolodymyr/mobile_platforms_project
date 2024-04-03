package com.example.project

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.adapter.EventAdapter
import com.example.project.model.EventModel

class EventActivity : AppCompatActivity() {

    private lateinit var adapter: EventAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        recyclerView = findViewById(R.id.recyclerViewEvent)
        initialize()
    }

    private fun initialize() {
        recyclerView = findViewById(R.id.recyclerViewEvent)
        adapter = EventAdapter(generateEventList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun generateEventList(): List<EventModel> {
        val eventList = ArrayList<EventModel>()
        eventList.add(EventModel("Meeting", "Team meeting at 10:00 AM"))
        eventList.add(EventModel("Lunch", "Lunch break at 12:30 PM"))
        eventList.add(EventModel("Presentation", "Presentation session at 2:00 PM"))
        eventList.add(EventModel("Training", "Training session at 3:30 PM"))
        eventList.add(EventModel("Discussion", "Discussion group at 5:00 PM"))
        eventList.add(EventModel("Networking", "Networking event at 7:00 PM"))
        eventList.add(EventModel("Closing Ceremony", "Closing ceremony at 9:00 PM"))
        eventList.add(EventModel("After Party", "After party celebration at 10:00 PM"))
        eventList.add(EventModel("Breakfast", "Breakfast meeting at 8:00 AM"))
        eventList.add(EventModel("Workshop", "Workshop session at 11:00 AM"))
        return eventList
    }
}
