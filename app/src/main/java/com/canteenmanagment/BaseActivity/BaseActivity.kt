package com.canteenmanagment.BaseActivity

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class BaseActivity : AppCompatActivity() {

    val scope = CoroutineScope(Dispatchers.Main)

}