package com.canteenmanagment.ui.CartFoodList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.databinding.ItemCartFoodLayoutBinding
import com.canteenmanagment.databinding.ItemFoodListLayoutBinding

class CartFoodListRecyclerViewAdapter(val cartFoodList : List<CartFood>) : RecyclerView.Adapter<CartFoodListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCartFoodLayoutBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.TVTitle.text = "${cartFoodList.get(position).food.name} (${cartFoodList.get(position).food.price} Rs.)"
        holder.binding.TVQuantity.text = "X   ${cartFoodList.get(position).qunity}"
    }

    override fun getItemCount(): Int {
        return cartFoodList.size
    }

    class ViewHolder(var binding: ItemCartFoodLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}