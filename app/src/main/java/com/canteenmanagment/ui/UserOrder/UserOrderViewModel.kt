package com.canteenmanagment.ui.UserOrder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.canteenmanagment.canteen_managment_library.apiManager.FirebaseApiManager
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.canteen_managment_library.models.Order
import com.canteenmanagment.helper.PastOrder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class UserOrderViewModel(activity: Application) : AndroidViewModel(activity) {

    var userPastOrderList: LiveData<List<Order>> = liveData {
        FirebaseApiManager.getAllPastOrders().let {
            if (it.isSuccess)
                emit(it.data as List<Order>)
            else
                emit(listOf<Order>())
        }
    }

    var userInProgressOrder : LiveData<List<Order>> = liveData {
        FirebaseApiManager.getInProgressOrder().let {
            if (it.isSuccess)
                emit(it.data as List<Order>)
            else
                emit(listOf<Order>())
        }
    }

    var userReadyOrder : LiveData<List<Order>> = liveData {
        FirebaseApiManager.getReadyOrder().let {
            if (it.isSuccess)
                emit(it.data as List<Order>)
            else
                emit(listOf<Order>())
        }
    }

    fun getHashmapFromList(orderList: List<Order>): HashMap<String, MutableList<PastOrder>> {
        var map: LinkedHashMap<String, MutableList<PastOrder>> = LinkedHashMap()

        for (order in orderList) {

            val cal: Calendar = Calendar.getInstance()
            cal.timeInMillis = order.time ?: 0
            val f = SimpleDateFormat("dd MMM,YYYY")
            val date = f.format(cal.time)
            val f2 = SimpleDateFormat("hh:mm aa")
            val time = f2.format(cal.time)

            val pastOrder: PastOrder = PastOrder(
                id = order.id,
                transactionId = order.transactionId,
                foodList = order.foodList,
                status = order.status,
                time = time,
                price = calculateTotalAmount(order.foodList!!)
            )

            if (map[date] == null) {
                val list = mutableListOf<PastOrder>()
                list.add(pastOrder)
                map[date] = list
            } else {
                val list = map[date]!!
                list.add(0,pastOrder)
                map[date] = list
            }

        }

        return map
    }

    fun getPastOrderListFromOrderList(orderList : List<Order>) : MutableList<PastOrder> {

        var pastOrderList : MutableList<PastOrder> = mutableListOf()

        for(order in orderList){
            val cal: Calendar = Calendar.getInstance()
            cal.timeInMillis = order.time ?: 0
            val f = SimpleDateFormat("dd MMM,YYYY")
            val date = f.format(cal.time)
            val f2 = SimpleDateFormat("hh:mm aa")
            val time = f2.format(cal.time)

            val pastOrder: PastOrder = PastOrder(
                id = order.id,
                transactionId = order.transactionId,
                foodList = order.foodList,
                status = order.status,
                time = time,
                price = calculateTotalAmount(order.foodList!!)
            )

            pastOrderList.add(pastOrder)
        }

        return pastOrderList
    }

    private fun calculateTotalAmount(cartFoodList: MutableList<CartFood>): Int {
        var total = 0
        for (cartFood in cartFoodList)
            total += cartFood.quantity * cartFood.food.price!!

        return total
    }

}