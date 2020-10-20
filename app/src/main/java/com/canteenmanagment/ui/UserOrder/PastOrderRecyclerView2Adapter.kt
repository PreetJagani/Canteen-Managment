package com.canteenmanagment.ui.UserOrder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canteenmanagment.databinding.ItemPastOrderLayout2Binding
import com.canteenmanagment.helper.PastOrder

class PastOrderRecyclerView2Adapter(val pastOrderList : MutableList<PastOrder>) : RecyclerView.Adapter<PastOrderRecyclerView2Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPastOrderLayout2Binding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.TVOrderId.text = pastOrderList[position].id
        holder.binding.TVTransactionId.text = pastOrderList[position].transactionId
        holder.binding.TVPrice.text = "${pastOrderList[position].price.toString()} Rs."
        holder.binding.TVTime.text = pastOrderList[position].time

        var foodListString = ""
        for(cartFood in pastOrderList[position].foodList!!)
            foodListString += "\n${cartFood.food.name} X ${cartFood.quantity}"

        holder.binding.TVOrderList.text = foodListString

    }

    override fun getItemCount(): Int {
        return pastOrderList.size
    }

    class ViewHolder(var binding: ItemPastOrderLayout2Binding) : RecyclerView.ViewHolder(binding.root)


}