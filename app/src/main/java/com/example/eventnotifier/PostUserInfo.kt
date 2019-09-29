package com.example.eventnotifier


/**
 * Created by RITWIK JHA on 01-02-2018.
 */
class PostUserInfo{
    var name:String?=null
    var url:String?= null
    var enroll:String?=null

    constructor(name:String,url:String,enroll:String){
        this.name=name
        this.url = url
        this.enroll = enroll
    }
}