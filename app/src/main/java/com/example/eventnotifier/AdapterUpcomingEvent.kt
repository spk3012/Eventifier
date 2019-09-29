package com.example.eventnotifier

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AdapterUpcomingEvent(private val eventList:ArrayList<Event>, private val context: Context): RecyclerView.Adapter<AdapterUpcomingEvent.UpcomingViewHolder>(){

    var mAuth: FirebaseAuth?=null
    var database= FirebaseDatabase.getInstance()!!
    var myRef= database.reference!!

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UpcomingViewHolder{
        val myView= LayoutInflater.from(context).inflate(R.layout.card_requested_events,parent,false)
        mAuth= FirebaseAuth.getInstance()
        return UpcomingViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        holder.BindItem(eventList[position])

        holder.buVerify.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Event Registration")
            alertDialog.setMessage("Are you sure you want to register for this event?")
            alertDialog.setPositiveButton(
                "CANCEL"
            ) { dialog, which -> dialog.cancel() }
            alertDialog.setNegativeButton("YES") { dialog, which ->
                myRef.child("Users").child("User Events").child(mAuth!!.currentUser!!.uid).child(holder.name.text.toString()).setValue(Event(holder.name.text.toString().trim(), holder.venue.text.toString().trim(), holder.date.text.toString().trim(), holder.uid!!, holder.email!!))
            }

            val dialog = alertDialog.create()
            dialog.show()
        }
    }

    class UpcomingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var buVerify: Button = itemView.findViewById(R.id.buVerify) as Button
        var name: TextView =itemView.findViewById(R.id.tvCardEventName) as TextView
        var venue: TextView =itemView.findViewById(R.id.tvCardVenue) as TextView
        var date: TextView =itemView.findViewById(R.id.tvCardDate) as TextView
        var uid:String?=null
        var email:String?=null

        fun BindItem(event:Event){
            name.text=event.name
            venue.text= event.venue
            date.text = event.date
            uid = event.uid
            email = event.email
            buVerify.text = "Register"
        }
    }

}