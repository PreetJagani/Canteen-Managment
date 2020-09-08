package com.canteenmanagment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.canteenmanagment.R
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.activity_sign_up_activity.*

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)



        /*btn_sign_out.setOnClickListener{
            //Sign Out
            AuthUI.getInstance().signOut(this).addOnCompleteListener{
                btn_sign_out.isEnabled=false
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }.addOnFailureListener{
                    e -> Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()
            }
        }*/
    }
}