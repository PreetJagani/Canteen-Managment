package com.canteenmanagment.ui.CartFoodList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class CartFoodListViewModel(application : Application) : AndroidViewModel(application) {

    /*var cartFoodList : LiveData<MutableList<CartFood>> = liveData{

        val preference = application.getSharedPreferences(FoodListActivity.CART, 0x0000)

        var gson = Gson()
        val type: Type = object : TypeToken<MutableList<CartFood>?>() {}.type

        var cartItemString = preference.getString(FoodListActivity.CART_ITEMS, null)

        var cartItemLIst = if (cartItemString != null)
            gson.fromJson<MutableList<CartFood>>(cartItemString, type)
        else
            mutableListOf()

        emit(cartItemLIst)

    }*/

    var cartFoodList : MutableLiveData<MutableList<CartFood>> = MutableLiveData()



    fun changeCartFoodList(list : MutableList<CartFood>){
        cartFoodList.postValue(list)
    }



}