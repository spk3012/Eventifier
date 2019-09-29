package com.example.eventnotifier

/**
 * Created by RITWIK JHA on 01-02-2018.
 */
class Event{
    var name:String?=null
    var venue:String?= null
    var date:String?=null
    var uid:String?=null
    var email:String?=null

    constructor(name:String,venue:String,date:String, uid:String, email:String){
        this.name=name
        this.venue = venue
        this.date = date
        this.uid = uid
        this.email = email
    }
}