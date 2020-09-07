package com.canteenmanagment

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up_activity.*
import java.util.Arrays.asList
import com.firebase.ui.auth.IdpResponse.fromResultIntent as fromResultIntent1

class SignUp_activity : AppCompatActivity() {

    lateinit var providers : List<AuthUI.IdpConfig>
    private  val MY_REQUEST_CODE : Int = 2332

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_activity)



        //Init
        providers = asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build()  //Email Login

        )

        showSignInOption()

        //Event
        btn_sign_out.setOnClickListener{
            //Sign Out
            AuthUI.getInstance().signOut(this).addOnCompleteListener{
                btn_sign_out.isEnabled=false

                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)


            }.addOnFailureListener{
                e -> Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
            }
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==MY_REQUEST_CODE)
        {
            val response = fromResultIntent1(data)

            if( FirebaseAuth.getInstance().currentUser == null)
            {
                Toast.makeText(this,"Please Enter Email id",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SignUp_activity::class.java)
                startActivity(intent)

            }
            else {
                if (resultCode == Activity.RESULT_OK) {
                    val user = FirebaseAuth.getInstance().currentUser  //   get current user.
                    Toast.makeText(this, "" + user?.email, Toast.LENGTH_SHORT).show()

                    btn_sign_out.isEnabled = true


                } else {
                    Toast.makeText(this, " Please Enter email id" + response?.error?.message, Toast.LENGTH_SHORT).show()
                    finish()
                    Intent(this, SignUp_activity::class.java)
                }
            }
        }
    }

    private fun showSignInOption() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
            .build(),MY_REQUEST_CODE)


    }
}