package com.canteenmanagment.Fragment.Home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.canteenmanagment.canteen_managment_library.apiManager.FirebaseApiManager
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.helper.getCurrentHour
import com.canteenmanagment.helper.getCurrentMinute
import com.canteenmanagment.helper.getTimeLabel

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {

    var pastFoodList: LiveData<List<Food>> = liveData {
        FirebaseApiManager.getAllPastFoods().let {
            if (it.isSuccess)
                emit(it.data as List<Food>)
            else
                emit(listOf<Food>())
        }
    }

    var favFoodList: LiveData<List<Food>> = liveData {
        FirebaseApiManager.getAllFavouriteFoods().let {
            if (it.isSuccess)
                emit(it.data as List<Food>)
            else
                emit(listOf<Food>())
        }
    }

    var foodList: LiveData<List<Food>> = liveData {

        val timeLabel = getTimeLabel(getCurrentHour(), getCurrentMinute())
        //val timeLabel = "Afternoon"
        if(!timeLabel.equals("Close"))
            FirebaseApiManager.getFoodListFromTime(timeLabel)
                .let {
                    if (it.isSuccess)
                        emit(it.data as List<Food>)
                    else
                        emit(listOf<Food>())
                }
        else
            emit(listOf<Food>())

    }


}