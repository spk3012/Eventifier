package com.example.eventnotifier

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TabCreatedEvents:Fragment(){

    private var adapterCreatedEvent:AdapterCreatedEvent?=null
    private var eventList= mutableSetOf<Event>()
    private var flagList=ArrayList<Int>()
    private var layoutManager: RecyclerView.LayoutManager?=null

    var mAuth: FirebaseAuth?=null

    var database= FirebaseDatabase.getInstance()
    var myRef=database.reference
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mAuth= FirebaseAuth.getInstance()
        LoadEvent()
        var view = inflater.inflate(R.layout.tab_created_events, container, false)
        var rv = view.findViewById(R.id.rvCreated) as RecyclerView

        adapterCreatedEvent= AdapterCreatedEvent(eventList,flagList, context!!)
        layoutManager= LinearLayoutManager(context)

        rv.layoutManager = layoutManager
        rv.adapter = adapterCreatedEvent
        return view
    }

    fun LoadEvent(){

        eventList.clear()
        flagList.clear()

        myRef.child("Events").child("Pending Events").child(mAuth!!.currentUser!!.uid)
            .addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }

                override fun onDataChange(p0: DataSnapshot?) = try {

                    var td= p0!!.value as HashMap<String,Any>
                    for (key in td.keys){
                        var eventDetails=td[key] as HashMap<String,Any>

                        eventList.add(
                            Event(eventDetails!!["name"] as String, eventDetails["venue"] as String, eventDetails["date"] as String, eventDetails["uid"] as String, eventDetails["email"] as String)
                        )
                        flagList.add(0)
                    }
                    adapterCreatedEvent!!.notifyDataSetChanged()
                }catch (ex:Exception){ }

            })

        myRef.child("Events").child("Verified Events").child(mAuth!!.currentUser!!.uid)
            .addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }

                override fun onDataChange(p0: DataSnapshot?) = try {

                    var td= p0!!.value as HashMap<String,Any>
                    for (key in td.keys){
                        var eventDetails=td[key] as HashMap<String,Any>

                        eventList.add(
                            Event(eventDetails!!["name"] as String, eventDetails["venue"] as String, eventDetails["date"] as String, eventDetails["uid"] as String, eventDetails["email"] as String)
                        )
                        flagList.add(1)
                    }
                    adapterCreatedEvent!!.notifyDataSetChanged()
                }catch (ex:Exception){ }

            })
    }
}