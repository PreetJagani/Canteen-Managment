package com.canteenmanagment.helper

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

fun showShortToast(mContext : Context,message : String){
    Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show()
}

fun getCurrentHour() : Int{

    val cal: Calendar = Calendar.getInstance()
    val hourFormatter = SimpleDateFormat("hh")
    var hour = hourFormatter.format(cal.time).toInt()

    val f = SimpleDateFormat("aa")
    val timeZone = f.format(cal.time)

    if(timeZone.toLowerCase().equals("pm"))
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

    val MORNING_START_HOUR = 8
    val MORNING_START_MINUTE = 30
    val AFTERNOON_START_HOUR = 11
    val EVENING_START_HOUR = 15
    val EVENING_END_HOUR = 18

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

