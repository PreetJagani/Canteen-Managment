package com.canteenmanagment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.canteenmanagment.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}