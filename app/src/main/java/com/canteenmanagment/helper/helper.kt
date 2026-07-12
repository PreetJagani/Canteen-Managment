package com.canteenmanagment.helper

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

fun showShortToast(mContext : Context,message : String){
    Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show()
}

/**
 * Android 15+ (targetSdk 35+) draws content edge-to-edge unconditionally, so screens
 * that relied on the theme reserving space for the status/navigation bars need to pad
 * for those insets themselves.
 */
fun View.applySystemBarInsets(top: Boolean = true, bottom: Boolean = true) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.updatePadding(
            top = if (top) bars.top else v.paddingTop,
            bottom = if (bottom) bars.bottom else v.paddingBottom
        )
        insets
    }
}

fun getCurrentHour() : Int{

    val cal: Calendar = Calendar.getInstance()
    val hourFormatter = SimpleDateFormat("hh")
    var hour = hourFormatter.format(cal.time).toInt()

    val f = SimpleDateFormat("aa")
    val timeZone = f.format(cal.time)

    if(timeZone.lowercase().equals("pm"))
        if(hour != 12)
            hour += 12

    return hour
}

fun getCurrentMinute() : Int{
    val cal: Calendar = Calendar.getInstance()
    val minFormatter = SimpleDateFormat("mm")
    val minute = minFormatter.format(cal.time).toInt()

    return minute
}

fun getTimeLabel(hour : Int, minute : Int) : String{

    val MORNING_START_HOUR = 6
    val MORNING_START_MINUTE = 0
    val AFTERNOON_START_HOUR = 11
    val EVENING_START_HOUR = 17
    val EVENING_END_HOUR = 23

    if(hour in MORNING_START_HOUR until AFTERNOON_START_HOUR){
        if(hour == MORNING_START_HOUR && minute<MORNING_START_MINUTE)
            return "Close"
        return "Morning"
    }
    else if (hour in AFTERNOON_START_HOUR until EVENING_START_HOUR){
        return "Afternoon"
    }
    else if(hour in EVENING_START_HOUR until EVENING_END_HOUR){
        return "Evening"
    }
    else{
        return "Close"
    }
}

