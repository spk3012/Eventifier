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

class TabRequestedEvents: Fragment(){

    private var adapterRequestedEvent:AdapterRequestedEvent?=null
    private var eventList=ArrayList<Event>()
    private var layoutManager: RecyclerView.LayoutManager?=null

    var mAuth: FirebaseAuth?=null

    var database= FirebaseDatabase.getInstance()
    var myRef=database.reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mAuth= FirebaseAuth.getInstance()
        LoadEvent()
        var view = inflater.inflate(R.layout.tab_requested_events, container, false)
        var rv = view.findViewById(R.id.rvRequested) as RecyclerView

        adapterRequestedEvent= AdapterRequestedEvent(eventList, context!!)
        layoutManager= LinearLayoutManager(context)

        rv.layoutManager = layoutManager
        rv.adapter = adapterRequestedEvent
        return view
    }

    fun LoadEvent(){

        myRef.child("Events").child("Pending Events")
            .addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }

                override fun onDataChange(p0: DataSnapshot?) = try {

                    eventList.clear()

                    var td= p0!!.value as HashMap<String,Any>
                    for (key in td.keys){
                        var events=td[key] as HashMap<String,Any>

                        for (k in events.keys){
                            var eventDetails=events[k] as HashMap<String, String>
                            eventList.add(
                                Event(eventDetails!!["name"] as String, eventDetails["venue"] as String, eventDetails["date"] as String, eventDetails["uid"] as String, eventDetails["email"] as String)
                            )
                        }
                    }
                    adapterRequestedEvent!!.notifyDataSetChanged()
                }catch (ex:Exception){ }

            })
    }
}