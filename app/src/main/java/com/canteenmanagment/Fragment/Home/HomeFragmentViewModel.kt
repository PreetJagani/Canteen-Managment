package com.canteenmanagment.Fragment.Home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.canteenmanagment.canteen_managment_library.apiManager.FirebaseApiManager
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.canteen_managment_library.models.Order

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {

    var foodList: LiveData<List<Food>> = liveData {
        FirebaseApiManager.getAllPastFoods().let {
            if (it.isSuccess)
                emit(it.data as List<Food>)
            else
                emit(listOf<Food>())
        }
    }



}