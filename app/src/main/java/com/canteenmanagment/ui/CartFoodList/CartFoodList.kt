package com.canteenmanagment.ui.CartFoodList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_cart_food_list.*
import java.lang.reflect.Type

class CartFoodList : AppCompatActivity() {

    private lateinit var cartFoodListViewModel : CartFoodListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_food_list)

        cartFoodListViewModel = ViewModelProviders.of(this).get(CartFoodListViewModel::class.java)

        getDataFromSharedPrefrences()

        cartFoodListViewModel.cartFoodList.observe(this, Observer {

            Log.d("Cart",it.toString())
            Toast.makeText(this,"Observe",Toast.LENGTH_SHORT).show()

        })

        BT_change.setOnClickListener {
            cartFoodListViewModel.changeCartFoodList(mutableListOf())
        }


    }

    fun getDataFromSharedPrefrences(){

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
}