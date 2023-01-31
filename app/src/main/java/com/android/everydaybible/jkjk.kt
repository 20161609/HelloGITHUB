package com.android.everydaybible

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.transition.Slide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime



class jkjk {
    var Date_Year = 0
    var Date_Month = 0
    var Date_Day = 0
    var GetDaysInMonth = arrayOf(
        31,28,31,30,31,30,31,31,30,31,30,31
    )

    var is_first_Clicked = false
    var network_connection = false
}