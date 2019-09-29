package com.example.eventnotifier

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_event_creation.*
import java.util.*

class EventCreationActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null

    private var database= FirebaseDatabase.getInstance()!!
    private var myRef= database.reference!!


    var eventVenue: String?=null
    var date:String?=null
    var time:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth= FirebaseAuth.getInstance()
        setContentView(R.layout.activity_event_creation)

        var adapter = ArrayAdapter.createFromResource(this,R.array.venue_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        venueSpinner.adapter = adapter
        venueSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                eventVenue = p0!!.getItemAtPosition(0).toString()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                eventVenue= p0!!.getItemAtPosition(p2).toString()
            }
        }

        bu_create_event.setOnClickListener {
            if (event_name.text.toString()!="") {
                var event_date = "$date || $time"
                myRef.child("Events").child("Pending Events").child(SplitString(mAuth!!.currentUser!!.uid)).child(event_name.text.toString()).setValue(Event(event_name.text.toString().trim(), eventVenue!!.trim(), event_date!!.trim(), mAuth!!.currentUser!!.uid, mAuth!!.currentUser!!.email.toString()))

                finish()
            }

        }

        buDate.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                date = "$dayOfMonth/$month/$year"
                buDate.text = date
            },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))

            datePicker.show()

        }

        buTime.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ view, hourOfDay, minute ->
                time = "$hourOfDay:$minute"
                buTime.text = time
            },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)

            timePicker.show()
        }
    }

    fun SplitString(str:String):String{
        var split=str.split("@")
        return split[0]
    }
}

