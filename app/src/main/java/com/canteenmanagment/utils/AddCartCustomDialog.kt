package com.canteenmanagment.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.databinding.AddCartCustomeDiologBinding
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.like.LikeButton
import com.like.OnLikeListener
import java.lang.reflect.Type

class AddCartCustomDialog(private val activity: Activity, val addToFav : (food : Food) -> Unit = {}, val removeFromFav : (food: Food) -> Unit = {}) {

    lateinit var alertDialog: Dialog
    private val MAX_ITEM = 5
    private val MIN_ITEM = 0
    private val preference: SharedPreferences? = activity.getSharedPreferences(
        FoodListActivity.CART,
        0x0000
    )

    private var gson = Gson()
    private val type: Type = object : TypeToken<MutableList<CartFood>?>() {}.type

    fun startDialog(
        food: Food,
        flag: Boolean = false,
        function: () -> Unit = {},
        isFavorite: Boolean = false,
        isFavVisible: Boolean = false
    ) {

        val dialog = AlertDialog.Builder(activity)

        val binding = AddCartCustomeDiologBinding.inflate(LayoutInflater.from(activity))
        val view = binding.root

        if(!isFavVisible)
            binding.BTFav.visibility = View.INVISIBLE

        if(isFavorite)
            binding.BTFav.isLiked = true

        binding.IMPlus.setOnClickListener {

            if (binding.TVItemCount.text.toString().toInt() < MAX_ITEM) {
                YoYo.with(Techniques.BounceInUp).duration(500).playOn(binding.TVItemCount)
                binding.TVItemCount.text =
                    (binding.TVItemCount.text.toString().toInt() + 1).toString()

                updateCart(binding.TVItemCount.text.toString().toInt(), food)
                if (flag)
                    function()
            }

        }

        binding.IMMinus.setOnClickListener {
            if (binding.TVItemCount.text.toString().toInt() > MIN_ITEM) {
                YoYo.with(Techniques.BounceInDown).duration(500).playOn(binding.TVItemCount)
                binding.TVItemCount.text =
                    (binding.TVItemCount.text.toString().toInt() - 1).toString()

                updateCart(binding.TVItemCount.text.toString().toInt(), food)
                if (flag)
                    function()
            }
        }

        binding.BTFav.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                addToFav(food)
            }
            override fun unLiked(likeButton: LikeButton) {
                removeFromFav(food)
            }
        })

        binding.TVName.text = food.name
        binding.TVPrice.text = "(${food.price}  Rs.)"

        binding.IMClose.setOnClickListener {
            stopDiaolog()
        }

        dialog.setView(view)

        alertDialog = dialog.create()
        alertDialog.show()
        alertDialog.setCancelable(true)
        alertDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)


        val cartItemString = preference?.getString(FoodListActivity.CART_ITEMS, null)

        val cartItemLIst = if (cartItemString != null)
            gson.fromJson<List<CartFood>>(cartItemString, type)
        else
            emptyList()

        for (cartItem in cartItemLIst)
            if (cartItem.food.id.equals(food.id)) {
                binding.TVItemCount.text = cartItem.quantity.toString()
                break
            }
    }

    fun stopDiaolog() {
        alertDialog.dismiss()
    }

    private fun updateCart(quantity: Int, food: Food) {

        val cartItemString = preference?.getString(FoodListActivity.CART_ITEMS, null)

        val cartItemList = if (cartItemString != null)
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

        val editor = preference?.edit()

        val cartListString = gson.toJson(cartItemList)
        editor?.putString(FoodListActivity.CART_ITEMS, cartListString)

        editor?.apply()
    }

}