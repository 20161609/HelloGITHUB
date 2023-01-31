package com.android.everydaybible

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.ModelLoaderRegistry
import com.bumptech.glide.module.AppGlideModule
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase.getInstance
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


import java.io.InputStream
import java.util.Calendar.getInstance

@GlideModule
class MyGlideApp: AppGlideModule(){}


//https://cocatv.tistory.com/480 glideApp사용법

//https://naver.me/5zoHQmX7 model loader해결 실마리.

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val boba = findViewById<ImageView>(R.id.boba)
        // Create a storage reference from our app
        val storage = Firebase.storage
        val storageRef = storage.reference

        var islandRef = storageRef.child("20220911.jpeg")

        val ONE_MEGABYTE: Long = 1080 * 1731

        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            Log.e("FUCK",islandRef.toString())
            Glide.with(this)
                .load(islandRef)
                //.skipMemoryCache(true)
                .centerCrop()
                .error(R.drawable.word0)
                .into(boba)
         // Data for "images/island.jpg" is returned, use this as needed
        }.addOnFailureListener {
            // Handle any errors
            Log.e("getBytes","failure")
        }
    }
}