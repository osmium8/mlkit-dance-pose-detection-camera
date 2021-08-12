package com.example.reba.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.reba.R
import com.example.reba.ui.CameraFragment.Companion.cameraDataClass
import com.example.reba.ui.VideoFragment.Companion.dataClass

class Result : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val textView: TextView = findViewById(R.id.textView)
        textView.text = dataClass?.listPose?.size.toString() + " posePoints"
        val textView2: TextView = findViewById(R.id.textView2)
        textView2.text = cameraDataClass?.listPose?.size.toString() + " posePoints"
    }
}