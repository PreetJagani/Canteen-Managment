package com.canteenmanagment.helper

import android.content.Context
import android.widget.Toast

fun showShortToast(mContext : Context,message : String){
    Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show()
}