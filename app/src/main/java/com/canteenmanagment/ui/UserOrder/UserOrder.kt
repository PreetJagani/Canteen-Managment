package com.canteenmanagment.ui.UserOrder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.canteenmanagment.R
import com.canteenmanagment.databinding.ActivityUserOrderBinding

class UserOrder : AppCompatActivity() {

    private lateinit var binding : ActivityUserOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_order)
        setContentView(binding.root)



    }
}