package com.canteenmanagment.helper

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.models.Food
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.add_cart_custome_diolog.view.*

class AddCartCustomDiolog(val activity : Activity) {

    lateinit var alertDialog : Dialog
    private val MAX_ITEM = 5
    private val MIN_ITEM = 0

    fun startDialog(food : Food){

        var dialog = AlertDialog.Builder(activity)

        var view = LayoutInflater.from(activity).inflate(R.layout.add_cart_custome_diolog,null)

        view.IM_plus.setOnClickListener {
            if(view.TV_item_count.text.toString().toInt() < MAX_ITEM){
                YoYo.with(Techniques.BounceInUp).duration(500).playOn(view.TV_item_count)
                view.TV_item_count.text = (view.TV_item_count.text.toString().toInt() + 1).toString()
            }

        }
        view.IM_minus.setOnClickListener {
            if(view.TV_item_count.text.toString().toInt() > MIN_ITEM){
                YoYo.with(Techniques.BounceInDown).duration(500).playOn(view.TV_item_count)
                view.TV_item_count.text = (view.TV_item_count.text.toString().toInt() -1).toString()
            }
        }
        view.TV_name.text = food.name
        view.TV_price.text ="(${food.price}  Rs.)"

        dialog.setView(view)

        alertDialog = dialog.create()
        alertDialog.show()
        alertDialog.setCancelable(true)
        alertDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)



    }

    fun stopDiaolog(){
        alertDialog.dismiss()
    }
}