package com.example.eventnotifier

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_category_selection.*
import kotlinx.android.synthetic.main.slide_layout.view.*
import java.util.*

class CategorySelectionActivity : AppCompatActivity() {

    var PATH_TOS=""
    var RC_SIGN_IN = 200

    var myPrefName="MyPref_File"
    var prefs: SharedPreferences?=null


    var category=0

    private var mAuth: FirebaseAuth?=null

    private var adapter:SliderAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth= FirebaseAuth.getInstance()
        prefs=applicationContext.getSharedPreferences(myPrefName, Context.MODE_PRIVATE)
        var storedCategory=prefs!!.getString("Category",null)
        if (isUserLogin()){
            when(storedCategory){
                "Admin"->{
                    AdminLogin()
                }
                "User"->{
                    UserLogin()
                }
            }
        }
        setContentView(R.layout.activity_category_selection)


        adapter= SliderAdapter(this)
        slideViewPager.adapter=adapter

        addDotsIndicator(0)

        slideViewPager.addOnPageChangeListener(viewListener)
    }

    inner class SliderAdapter: PagerAdapter {

        var context: Context?=null

        constructor(context: Context){
            this.context = context
        }

        var slideImages = intArrayOf(R.drawable.events, R.drawable.admin, R.drawable.user)

        var categoryName = arrayOf("Events", "Admin", "User")


        override fun getCount(): Int {
            return categoryName.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {


            var inflater=context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val myView = inflater.inflate(R.layout.slide_layout, container, false)

            myView.ivCategory.setImageResource(slideImages[position])
            myView.tvCategoryName.text=categoryName[position]

            if (categoryName[position]=="Admin"){
                var providers = Arrays.asList(
                    AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())

                myView.tvCategoryName.setOnClickListener {
                    var editor=prefs!!.edit()
                    editor.putString("Category","Admin")
                    editor.commit()
                    category=2
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                        .setTosUrl(PATH_TOS)
                        .setAvailableProviders(providers)
                        .build(),RC_SIGN_IN)
                }

                myView.ivCategory.setOnClickListener {
                    var editor=prefs!!.edit()
                    editor.putString("Category","Admin")
                    editor.commit()
                    category=2
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                        .setTosUrl(PATH_TOS)
                        .setAvailableProviders(providers)
                        .build(),RC_SIGN_IN)
                }

            }

            if (categoryName[position]=="User"){
                var providers = Arrays.asList(
                    AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())

                myView.tvCategoryName.setOnClickListener {
                    var editor=prefs!!.edit()
                    editor.putString("Category","User")
                    editor.commit()
                    category=3
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                        .setTosUrl(PATH_TOS)
                        .setAvailableProviders(providers)
                        .build(),RC_SIGN_IN)
                }

                myView.ivCategory.setOnClickListener {
                    var editor=prefs!!.edit()
                    editor.putString("Category","User")
                    editor.commit()
                    category=3
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                        .setTosUrl(PATH_TOS)
                        .setAvailableProviders(providers)
                        .build(),RC_SIGN_IN)
                }

            }



            container.addView(myView)

            return myView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

            container.removeView(`object` as RelativeLayout)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==RC_SIGN_IN){
            if (resultCode== Activity.RESULT_OK){
                when (category) {
                    2 -> AdminLogin()
                    3 -> UserLogin()
                }
            }
            if (resultCode== Activity.RESULT_CANCELED){
                Toast.makeText(applicationContext,"Login Failed", Toast.LENGTH_SHORT).show()
            }
            return
        }
    }

    var dot=1

    fun addDotsIndicator(position: Int) {
        val myDots: Array<TextView> = Array(3){ TextView(this) }

        if (dot==1){
            for (i in 0..2) {
                myDots[i] = TextView(this)
                myDots[i].text = Html.fromHtml("&#8226")
                myDots[i].textSize = 50f
                myDots[i].setTextColor(resources.getColor(R.color.transparentWhite))

                dotsLayout.addView(myDots[i])
            }
            dot=2
        }

        if (myDots.isNotEmpty()){
            myDots[position].setTextColor(resources.getColor(R.color.white))
        }



    }


    private var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            addDotsIndicator(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    fun isUserLogin():Boolean{
        if (mAuth!!.currentUser!=null){
            return true
        }
        return false
    }

    fun AdminLogin(){
        var intent= Intent(this,AdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun UserLogin(){
        var intent= Intent(this,UserActivity::class.java)
        startActivity(intent)
        finish()
    }
}
