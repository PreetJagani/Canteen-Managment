package com.canteenmanagment.ui.UserOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canteenmanagment.canteen_managment_library.models.Order
import com.canteenmanagment.databinding.ItemOngoingOrderLayoutBinding
import com.canteenmanagment.databinding.ItemPastOrderLayout2Binding
import com.canteenmanagment.helper.PastOrder

class OnGoingOrderRecyclerViewAdapter(val onGoingOrderList : MutableList<PastOrder>,val mContext : Context, val openOrderDetailDialog : (orderId : String, transacnId : String)->Unit) : RecyclerView.Adapter<OnGoingOrderRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemOngoingOrderLayoutBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.TVOrderId.text = onGoingOrderList[position].id
        holder.binding.TVTransactionId.text = onGoingOrderList[position].transactionId
        holder.binding.TVPrice.text = "${onGoingOrderList[position].price.toString()} Rs."
        holder.binding.TVTime.text = onGoingOrderList[position].time

        var foodListString = ""
        for(cartFood in onGoingOrderList[position].foodList!!)
            foodListString += "\n${cartFood.food.name} X ${cartFood.quantity}"

        holder.binding.TVOrderList.text = foodListString

        if (onGoingOrderList[position].status.equals(Order.Status.READY.value)){
            holder.binding.TVStatus.text = "Ready"
            holder.binding.TVStatus.setTextColor( mContext.resources.getColor(android.R.color.holo_green_dark))
            holder.binding.CL.setOnClickListener {
                openOrderDetailDialog(onGoingOrderList[position].id?:"",onGoingOrderList[position].transactionId?:"")
            }

        }
        else{
            holder.binding.TVStatus.text = "Preparing"
            holder.binding.TVStatus.setTextColor( mContext.resources.getColor(android.R.color.holo_orange_dark))
        }

    }

    override fun getItemCount(): Int {
        return onGoingOrderList.size
    }

    class ViewHolder(var binding: ItemOngoingOrderLayoutBinding) : RecyclerView.ViewHolder(binding.root)


}