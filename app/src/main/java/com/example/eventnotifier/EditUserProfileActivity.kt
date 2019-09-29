package com.example.eventnotifier

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_user_profile.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class EditUserProfileActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null

    private var database= FirebaseDatabase.getInstance()!!
    private var myRef= database.reference!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth= FirebaseAuth.getInstance()
        setContentView(R.layout.activity_edit_user_profile)

        ivUserAdd.setOnClickListener {
            CheckPermission()
        }

        buSave.setOnClickListener{
            if (etUserName.text.toString()!="") {

                myRef.child("Users").child("User Profile").child(mAuth!!.currentUser!!.uid).setValue(PostUserInfo(etUserName.text.toString().trim(),  DownloadUrl, etEnrollNo.text.toString().trim()))

                finish()
            }
        }
    }



    var READIMAGE:Int=253

    fun CheckPermission(){
        if (Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                return
            }
        }
        LoadImage()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            READIMAGE->{
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    LoadImage()
                }else{
                    Toast.makeText(applicationContext,"Can't Grant Permission", Toast.LENGTH_SHORT).show()
                }
            }
            else->{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    var PICK_IMAGE_CODE=123

    fun LoadImage(){
        var photoGallery= Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(photoGallery,PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==PICK_IMAGE_CODE && data!=null && resultCode== Activity.RESULT_OK){

            val selectedImage=data.data
            val filePathCol= arrayOf(MediaStore.Images.Media.DATA)
            var cursor=contentResolver.query(selectedImage,filePathCol,null,null,null)
            cursor.moveToFirst()
            val colIndex=cursor.getColumnIndex(filePathCol[0])
            val picturePath=cursor.getString(colIndex)
            cursor.close()

            UploadImage(BitmapFactory.decodeFile(picturePath))
        }
    }

    var DownloadUrl:String=""

    fun UploadImage(bitmap: Bitmap){

        val storage= FirebaseStorage.getInstance()
        val storageRef=storage.getReferenceFromUrl("gs://itachi-61933.appspot.com/")
        val df= SimpleDateFormat("ddMMyyHHmmss")
        val dateObj= Date()
        val imgPath=SplitString(etUserName.text.toString())+"."+ df.format(dateObj)+ ".jpg"
        val imgRef=storageRef.child("User/"+imgPath)
        var baos= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data=baos.toByteArray()
        val uploadTask=imgRef.putBytes(data)

        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext,"Uploading Image Failed", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            DownloadUrl=taskSnapshot.downloadUrl!!.toString()
            Picasso.with(this).load(DownloadUrl).into(ivUserAdd)
        }


    }

    fun SplitString(str:String):String{
        var split=str.split("@")
        return split[0]
    }
}
