package com.canteenmanagment.ui.UserOrder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canteenmanagment.databinding.ItemPastOrderLayout1Binding
import com.canteenmanagment.helper.PastOrder
import java.util.stream.Collectors

class PastOrderRecyclerViewAdapter(val pastOrderMap : HashMap<String,MutableList<PastOrder>>) : RecyclerView.Adapter<PastOrderRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPastOrderLayout1Binding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var date = pastOrderMap.keys.stream().collect(Collectors.toList())[position]
        holder.binding.TVDate.text = date
        holder.binding.RVPastOrder.adapter = PastOrderRecyclerView2Adapter(pastOrderMap[date]!!)
    }

    override fun getItemCount(): Int {
        return pastOrderMap.size
    }

    class ViewHolder(var binding: ItemPastOrderLayout1Binding) : RecyclerView.ViewHolder(binding.root)


}