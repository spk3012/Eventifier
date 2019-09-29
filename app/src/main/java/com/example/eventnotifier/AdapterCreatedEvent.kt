package com.example.eventnotifier

import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AdapterCreatedEvent(private val eventList:Set<Event>,private val flagList:ArrayList<Int>, private val context: Context): RecyclerView.Adapter<AdapterCreatedEvent.CreatedViewHolder>(){

    var mAuth: FirebaseAuth?=null
    var database= FirebaseDatabase.getInstance()!!
    var myRef= database.reference!!

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CreatedViewHolder{
        val myView= LayoutInflater.from(context).inflate(R.layout.card_requested_events,parent,false)
        mAuth= FirebaseAuth.getInstance()
        return CreatedViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: CreatedViewHolder, position: Int) {
        holder.BindItem(eventList.elementAt(position))

        if (flagList[position] == 0){
            holder.buVerify.text = "Pending"
            holder.buVerify.setBackgroundResource(R.drawable.bu_back_pending)
        }else{
            holder.buVerify.text = "Verified"
        }
    }

    class CreatedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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
        }
    }

}