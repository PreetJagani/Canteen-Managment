package com.canteenmanagment.ui.CartFoodList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.canteenmanagment.canteen_managment_library.models.CartFood

class CartFoodListViewModel(application: Application) : AndroidViewModel(application) {

    var cartFoodList: MutableLiveData<MutableList<CartFood>> = MutableLiveData()

    fun changeCartFoodList(list: MutableList<CartFood>) {
        cartFoodList.postValue(list)
    }

}