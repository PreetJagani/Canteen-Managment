package com.canteenmanagment.ui.CartFoodList

import android.os.Bundle
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.databinding.ActivityCartFoodListBinding
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class CartFoodList : AppCompatActivity() {

    private lateinit var cartFoodListViewModel : CartFoodListViewModel
    private lateinit var binding : ActivityCartFoodListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_food_list)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_cart_food_list
        )
        cartFoodListViewModel = ViewModelProviders.of(this).get(CartFoodListViewModel::class.java)

        binding.RVCartFood.setOnTouchListener(OnTouchListener { v, event -> true })

        getDataFromSharedPreferences()

        cartFoodListViewModel.cartFoodList.observe(this, Observer {
            binding.RVCartFood.adapter = CartFoodListRecyclerViewAdapter(it)
            binding.TVTotal.text = "= ${calculateTotalAmount(it)} Rs."
        })

    }

    private fun getDataFromSharedPreferences(){

        val preference = application.getSharedPreferences(FoodListActivity.CART, 0x0000)

        var gson = Gson()
        val type: Type = object : TypeToken<MutableList<CartFood>?>() {}.type

        var cartItemString = preference.getString(FoodListActivity.CART_ITEMS, null)

        var cartItemLIst = if (cartItemString != null)
            gson.fromJson<MutableList<CartFood>>(cartItemString, type)
        else
            mutableListOf()

        cartFoodListViewModel.cartFoodList.postValue(cartItemLIst)
    }

    private fun calculateTotalAmount(cartFoodList: MutableList<CartFood>) : Int{

        var total = 0
        for(cartFood in cartFoodList)
            total += cartFood.qunity * cartFood.food.price!!

        return total
    }
}