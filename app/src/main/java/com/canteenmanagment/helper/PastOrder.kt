package com.canteenmanagment.helper

import com.canteenmanagment.canteen_managment_library.models.CartFood

class PastOrder(
    var id: String?,
    var transactionId: String?,
    var foodList: MutableList<CartFood>?,
    var status: String?,
    var time: String?,
    var price : Int?
)