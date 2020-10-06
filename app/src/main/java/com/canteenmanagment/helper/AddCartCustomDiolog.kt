package com.canteenmanagment.helper

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.add_cart_custome_diolog.view.*
import java.lang.reflect.Type

class AddCartCustomDiolog(val activity: Activity) {

    lateinit var alertDialog: Dialog
    private val MAX_ITEM = 5
    private val MIN_ITEM = 0
    val prefrence = activity.getSharedPreferences(FoodListActivity.CART, 0x0000)

    var gson = Gson()
    val type: Type = object : TypeToken<MutableList<CartFood>?>() {}.type

    fun startDialog(food: Food, mContext: Context) {

        var dialog = AlertDialog.Builder(activity)

        var view = LayoutInflater.from(activity).inflate(R.layout.add_cart_custome_diolog, null)

        view.IM_plus.setOnClickListener {

            if (view.TV_item_count.text.toString().toInt() < MAX_ITEM) {
                YoYo.with(Techniques.BounceInUp).duration(500).playOn(view.TV_item_count)
                view.TV_item_count.text =
                    (view.TV_item_count.text.toString().toInt() + 1).toString()

                updateCart(view.TV_item_count.text.toString().toInt(), food)
            }

        }

        view.IM_minus.setOnClickListener {
            if (view.TV_item_count.text.toString().toInt() > MIN_ITEM) {
                YoYo.with(Techniques.BounceInDown).duration(500).playOn(view.TV_item_count)
                view.TV_item_count.text =
                    (view.TV_item_count.text.toString().toInt() - 1).toString()

                updateCart(view.TV_item_count.text.toString().toInt(), food)
            }
        }

        view.TV_name.text = food.name
        view.TV_price.text = "(${food.price}  Rs.)"

        dialog.setView(view)

        alertDialog = dialog.create()
        alertDialog.show()
        alertDialog.setCancelable(true)
        alertDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)


        var cartItemString = prefrence.getString(FoodListActivity.CART_ITEMS, null)

        var cartItemLIst = if (cartItemString != null)
            gson.fromJson<List<CartFood>>(cartItemString, type)
        else
            emptyList()

        for (cartItem in cartItemLIst)
            if (cartItem.food.id.equals(food.id)) {
                view.TV_item_count.text = cartItem.qunity.toString()
                break;
            }
    }

    fun stopDiaolog() {
        alertDialog.dismiss()
    }

    private fun updateCart(quantity: Int, food: Food) {

        var cartItemString = prefrence.getString(FoodListActivity.CART_ITEMS, null)

        var cartItemList = if (cartItemString != null)
            gson.fromJson<MutableList<CartFood>>(cartItemString, type)
        else
            mutableListOf()

        var flag = false
        for (cartItem in cartItemList)
            if (cartItem.food.id.equals(food.id)) {
                cartItemList.remove(cartItem)
                if (quantity != 0)
                    cartItemList.add(CartFood(quantity, food))
                flag = true
                break
            }

        if (!flag)
            cartItemList.add(CartFood(quantity, food))

        val editor = prefrence.edit()

        var cartListString = gson.toJson(cartItemList)
        editor.putString(FoodListActivity.CART_ITEMS, cartListString)

        editor.apply()

    }

}