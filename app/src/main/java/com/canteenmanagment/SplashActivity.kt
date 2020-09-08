package com.canteenmanagment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_activity)

        var user = FirebaseAuth.getInstance().currentUser
        var intent : Intent
        if(user==null)
            intent = Intent(this,MainActivity::class.java)
        else
            intent = Intent(this,HomePageActivity::class.java)

        handler = Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        },  2500)
    }
}
