package com.android.everydaybible

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime


/*전역 변수*/
var current_picture_index : Int = 6 //선택된 사진.
var DP : Int = 0
var PX : Int = 1
var SP : Int = 2

private lateinit var database: DatabaseReference
val storageReference = Firebase.storage.reference

var Date_Year = 0
var Date_Month = 0
var Date_Day = 0
var GetDaysInMonth = arrayOf(
    31,28,31,30,31,30,31,31,30,31,30,31
)

var is_first_Clicked = false
var network_connection = false
var is_Toast = false
var image_file_name = ""

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*뷰어 모음*/
        //Title "딩동 말씀"
        var TextV : TextView = findViewById(R.id.Title)

        // 뷰어들의 총 집합체
        val Entire_box : LinearLayout = findViewById(R.id.Entire_box)

        // 하단 메뉴바
        val card_box : androidx.cardview.widget.CardView = findViewById(R.id.card_box)

        // 전체 화면
        var screen : androidx.constraintlayout.widget.ConstraintLayout = findViewById(R.id.screen)

        // 앱정보
        val drawer : androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer)

        // 저장성공 메시지(토스트뷰) - "저장되었습니다"
        val success_message : androidx.cardview.widget.CardView = findViewById(R.id.success_message)
        Entire_box.removeView(success_message)

        // 날짜변환시 실패 메시지(토스트뷰) - "네트워크 연결을 확인해주세요"
        val fail_message : androidx.cardview.widget.CardView = findViewById(R.id.fail_message)
        Entire_box.removeView(fail_message)

        //출력되는 이미지.
        val imageView = findViewById<ImageView>(R.id.Current_picture)

        /*변수 모음*/
        // TODO ATTENTION. THIS CODE BLOCK IS NOT WORKING!

        /*함수 모음*/
        /*오늘의 날짜->텍스트변환*/
        fun Todays_Date(): String {
            val Today = LocalDateTime.now()
            val Year : String = Today.year.toString()
            val Month : String = Today.monthValue.toString()
            val Day : String = Today.dayOfMonth.toString()
            var week_day : String = ""
            when(Today.dayOfWeek.toString()){
                "SUNDAY" -> week_day = "일요일"
                "MONDAY" -> week_day = "월요일"
                "TUESDAY" -> week_day = "화요일"
                "WEDNESDAY" -> week_day = "수요일"
                "THURSDAY" -> week_day = "목요일"
                "FRIDAY" -> week_day = "금요일"
                "SATURDAY" -> week_day = "토요일"
            }
            val Today_Text : String = (Year+"년 "+Month+"월 "+Day+"일 "+ week_day)

            Date_Year = Year.toInt()
            Date_Month = Month.toInt()
            Date_Day = Day.toInt()
            if (Date_Year%4 == 0 && Date_Year%100 != 0 || Date_Year%400 == 0)
                GetDaysInMonth[1] = 29

            return Today_Text
        }

        /*image file name in firebase's storage*/
        fun image_file_name(){
            val Today = LocalDateTime.now()
            val year = Today.year.toString()
            var month = Today.month.toString()
            val date = Today.dayOfMonth
            if(Today.monthValue < 10)
                month = "0" + Today.month
            if(Today.dayOfMonth < 10)
                month = "0" + Today.dayOfMonth

            image_file_name = year + month + date + ".jpeg"
        }

        fun image_Caching(){
            val storage = Firebase.storage
            val storageRef = storage.reference
            var islandRef = storageRef.child(image_file_name)
            val ONE_MEGABYTE: Long = 1024 * 1024

            islandRef.downloadUrl.addOnSuccessListener { uri ->
                Log.e("FUCK",islandRef.toString())
                Glide.with(imageView.context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(imageView)
            }.addOnFailureListener {
                // Handle any errors
                Log.e("getBytes","failure")
            }
        }
        image_file_name()
        image_Caching()

        /*이미지 갱신*/
        fun Image_change(index : Int){
            when(index){
                0->imageView.setImageResource(R.drawable.word0)
                1->imageView.setImageResource(R.drawable.word1)
                2->imageView.setImageResource(R.drawable.word2)
                3->imageView.setImageResource(R.drawable.word3)
                4->imageView.setImageResource(R.drawable.word4)
                5->imageView.setImageResource(R.drawable.word5)
                6->imageView.setImageResource(R.drawable.word6)
            }
        }

        /*토스트뷰 출력시 버튼 클릭 제한.*/
        val mCountDown : CountDownTimer = object : CountDownTimer(2000, 5000) {
            override fun onTick(millisUntilFinished: Long) {
                is_Toast = true
            }

            override fun onFinish() {
                //countdown finish
                is_Toast = false
            }
        }

        /*버튼0. 어제 말씀*/
        fun button0_action(){
            val home2 = LinearLayout(this)
            home2.setBackgroundColor(Color.parseColor("#DDDDDD"))
            home2.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                230
            )
            home2.orientation = LinearLayout.VERTICAL

            val home2_0 = LinearLayout(this)
            val layoutParams2_0 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams2_0.setMargins(20,0,0,0)
            home2_0.layoutParams = layoutParams2_0
            home2_0.orientation = LinearLayout.HORIZONTAL

            val home2_0_0 = TextView(this)
            home2_0_0.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            home2_0_0.setText("최근 일주일 말씀입니다.")
            home2_0_0.setTypeface(home2_0_0.typeface, Typeface.BOLD)
            home2_0_0.setTextSize(DP, 37.toFloat())

            val home2_0_1 = androidx.appcompat.widget.AppCompatButton(this)
            val layoutParams2 = LinearLayout.LayoutParams(60, 60)
            layoutParams2.setMargins(350,20,0,0)
            home2_0_1.layoutParams = layoutParams2
            home2_0_1.setBackgroundResource(android.R.drawable.presence_offline)

            val home2_1 = LinearLayout(this)
            val layoutParams3 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            home2_1.layoutParams = layoutParams3
            home2_1.orientation = LinearLayout.VERTICAL

            val home2_1_0 = TextView(this)
            val layoutParams2_1_0 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams2_1_0.setMargins(35,10,0,0)
            home2_1_0.layoutParams = layoutParams2_1_0
            home2_1_0.setText("11월")
            home2_1_0.setTextSize(DP, 30.toFloat())

            val home2_2 = LinearLayout(this)
            home2_2.orientation = LinearLayout.HORIZONTAL
            home2_2.gravity = Gravity.CENTER
            home2_2.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0,20,0,0)
            }

            fun get_date_str(date_int : Int): String {
                var Date_int = date_int
                if(date_int <= 0){
                    Date_int = GetDaysInMonth[Date_Month - 1] - (date_int - 1)
                }

                if(Date_int >= 10)
                    return Date_int.toString()
                else
                    return "0".plus(Date_int.toString())
            }


            var Button_Set : Array<TextView> = arrayOf()
            for (i in 0..6){
                val date_button = TextView(this)//androidx.appcompat.widget.AppCompatButton(this)
                var Margin = 52
                if(i==0) Margin = 0

                date_button.layoutParams = LinearLayout.LayoutParams(
                    65,65
                ).apply {
                    setMargins(Margin,0,0,0)
                }
                if(is_first_Clicked && current_picture_index == i) {
                    date_button.setBackgroundResource(R.drawable.shape_for_circle_button_clicked)
                    date_button.setTextColor(Color.WHITE)
                }
                else{
                    date_button.setBackgroundResource(R.drawable.shape_for_circle_button)
                    date_button.setTextColor(Color.BLACK)
                }
                date_button.setGravity(Gravity.CENTER)
                date_button.isClickable = true
                date_button.text = get_date_str(Date_Day - 6 + i)
                date_button.setStateListAnimator(null)
                Button_Set = Button_Set.plus(date_button)
            }
            for(Clicked in 0..6){
                Button_Set[Clicked].setOnClickListener(){
                    if(network_connection){
                        Log.e(Clicked.toString(), "Clicked")
                        if(is_first_Clicked){
                            if(Clicked != current_picture_index){
                                /*기존버튼 해제*/
                                Button_Set[current_picture_index].setBackgroundResource(
                                    R.drawable.shape_for_circle_button
                                )
                                Button_Set[current_picture_index].setTextColor(Color.BLACK)

                                /*클릭된 버튼 표식*/
                                current_picture_index = Clicked
                                Button_Set[current_picture_index].setBackgroundResource(
                                    R.drawable.shape_for_circle_button_clicked
                                )
                                Image_change(current_picture_index)
                                Button_Set[current_picture_index].setTextColor(Color.WHITE)
                            }
                        }
                        else{
                            is_first_Clicked = true
                            current_picture_index = Clicked
                            Button_Set[current_picture_index].setBackgroundResource(
                                R.drawable.shape_for_circle_button_clicked
                            )
                            Button_Set[current_picture_index].setTextColor(Color.WHITE)
                            Image_change(current_picture_index)
                        }
                    }
                    else{
                        Toast(this).apply {
                            setGravity(Gravity.CENTER, 0, 550)//197)
                            view = fail_message
                            duration = Toast.LENGTH_SHORT
                        }.show().run{
                            mCountDown.start()
                        }
                    }
                }
                home2_2.addView(Button_Set[Clicked])
            }

            // Add all Views
            home2_0.addView(home2_0_0)
            home2_0.addView(home2_0_1)
            home2.addView(home2_0)

            home2_1.addView(home2_1_0)
            home2.addView(home2_1)

            home2.addView(home2_2)
            val home1 : LinearLayout = findViewById(R.id.button_box)

            card_box.removeAllViews()
            card_box.addView(home2)

            home2_0_1.setOnClickListener(){
                card_box.removeAllViews()
                card_box.addView(home1)
            }
        //@ change Orientation
        }

        /*버튼1. 저장*/
        @RequiresApi(Build.VERSION_CODES.O_MR1)
        fun button1_action() {
            network_connection = !network_connection

            val card_frame = androidx.cardview.widget.CardView(this).apply {
                //layoutParams = LinearLayout.LayoutParams(600,300)
                radius = card_box.radius
            }
            Log.e("asdf", card_box.radius.toString())
            val card_frame_linear = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    836,
                    230
                )
                setBackgroundColor(Color.RED)
                gravity = Gravity.CENTER
            }

            val Toast_Message = TextView(this).apply{
                text = "저장되었습니다."
                setTextSize(DP, 60.toFloat())
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                gravity = Gravity.CENTER
                setBackgroundColor(Color.BLUE)
            }
            card_frame_linear.addView(Toast_Message)
            card_frame.addView(card_frame_linear)

            var toast = Toast(this).apply {
                setGravity(Gravity.CENTER, 0, 550)
                view = success_message
                duration = Toast.LENGTH_SHORT
            }

            Toast(this).apply {
                setGravity(Gravity.CENTER, 0, 550)//197)
                view = success_message
                duration = Toast.LENGTH_SHORT
            }.show().run{
                mCountDown.start()
            }
        }

        /*버튼2. 공유*/
        fun button2_action(){
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        /*버튼3. 앱정보*/
        fun button3_action(){

            drawer.openDrawer(Gravity.LEFT)
        }

        /*현재 날짜 업로드*/
        val DateViewer : TextView = findViewById(R.id.DateTime)
        DateViewer.text = Todays_Date()

        /*말씀카드 출력*/
        Image_change(current_picture_index)

        /*버튼 셋팅*/
        val button_box: LinearLayout = findViewById(R.id.button_box)
        val button0 : Button = findViewById(R.id.button0000)//어제 말씀
        val button1 : Button = findViewById(R.id.button0001)//저장
        val button2 : Button = findViewById(R.id.button0002)//공유
        val button3 : Button = findViewById(R.id.button0003)//앱정보

        /*버튼 별 기능 활성화*/
        for((button_num, button) in arrayOf(button0, button1, button2, button3).withIndex()){
            button.setOnClickListener(){
                Log.e("Clicked", button.text.toString())
                if(drawer.isDrawerOpen(Gravity.LEFT) || is_Toast){
                    Log.e("FUCK", "YOU")
                }
                else{
                    when(button_num){
                        0->button0_action()//어제 말씀
                        1->button1_action()//저장
                        2->button2_action()//공유
                        3->button3_action()//앱정보
                    }
                    is_Toast = false
                }
            }
        }
    }
}