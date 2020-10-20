package com.canteenmanagment.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import com.canteenmanagment.R

class CustomProgressBar(val activity : Activity) {

    lateinit var alertDialog : Dialog

    fun startDialog(){

        var dialog = AlertDialog.Builder(activity)

        var view = LayoutInflater.from(activity).inflate(R.layout.progress_bar,null)

        dialog.setView(view)

        alertDialog = dialog.create()
        alertDialog.show()
        alertDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

    }
    fun stopDiaolog(){
        alertDialog.dismiss()
    }
}