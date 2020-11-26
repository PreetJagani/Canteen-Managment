package com.canteenmanagment.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.canteenmanagment.BaseActivity.BaseActivity
import com.canteenmanagment.Fragment.Home.HomeFragment
import com.canteenmanagment.Fragments.MenuFragment
import com.canteenmanagment.Fragments.ProfileFragment
import com.canteenmanagment.R
import com.canteenmanagment.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    private lateinit var binding : ActivityHomeBinding
    private var backPress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setContentView(binding.root)

        openFragment(HomeFragment())
        binding.chipNavigationBar.setItemSelected(R.id.home)
        backPress = 0

        binding.chipNavigationBar.setOnItemSelectedListener {

            when(it){
                R.id.home -> openFragment(HomeFragment())
                R.id.menu -> openFragment(MenuFragment())
                R.id.profile -> openFragment(ProfileFragment())
            }
        }

    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

    override fun onBackPressed() {
        if (backPress == 1) {
            finishAffinity()
        } else {
            Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show()
            backPress = 1
            val t1: CountDownTimer = object : CountDownTimer(2000, 1000) {
                override fun onTick(l: Long) {}
                override fun onFinish() {
                    backPress = 0
                }
            }
            t1.start()
        }

    }
}