package com.canteenmanagment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.canteenmanagment.ui.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_activity)

        val editor = this.getSharedPreferences(FoodListActivity.CART, 0x0000).edit()
        editor.remove(FoodListActivity.CART_ITEMS)
        editor.apply()

        var user = FirebaseAuth.getInstance().currentUser
        var intent : Intent
        if(user==null)
            intent = Intent(this,MainActivity::class.java)
        else
            intent = Intent(this, HomeActivity::class.java)

        handler = Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        },  2500)
    }
}
