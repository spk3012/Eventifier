package com.example.eventnotifier

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth?=null

    var database= FirebaseDatabase.getInstance()
    var myRef=database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth= FirebaseAuth.getInstance()
        setContentView(R.layout.activity_user_profile)

        LoadUserInfo()

        buEdit.setOnClickListener{
            var intent = Intent(this, EditUserProfileActivity::class.java)
            startActivity(intent)
        }
    }

    fun LoadUserInfo(){

        myRef.child("Users").child("User Profile")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }

                override fun onDataChange(p0: DataSnapshot?) {
                    var td=p0!!.value as HashMap<String,Any>

                    for (key in td.keys){
                        if(key == mAuth!!.currentUser!!.uid){
                            var user=td[key] as HashMap<String,Any>

                            var username=user["name"] as String
                            var enroll=user["enroll"] as String
                            var userImage=user["url"] as String

                            tvUserName.text=username
                            tvEmail.text=mAuth!!.currentUser!!.email.toString()
                            tvEnroll.text=enroll
                            Picasso.with(this@UserProfileActivity).load(userImage).into(ivUserShow)



                        }
                    }

                }

            })
    }
}
