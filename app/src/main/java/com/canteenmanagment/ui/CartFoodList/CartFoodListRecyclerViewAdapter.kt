package com.canteenmanagment.ui.CartFoodList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.databinding.ItemCartFoodLayoutBinding

class CartFoodListRecyclerViewAdapter(val cartFoodList : List<CartFood>,val openCustomeDiolog : (position : Int) -> Unit ) : RecyclerView.Adapter<CartFoodListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCartFoodLayoutBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.TVTitle.text = "${cartFoodList.get(position).food.name} (${cartFoodList.get(position).food.price} Rs.)"
        holder.binding.TVQuantity.text = "X   ${cartFoodList.get(position).quantity}"
        holder.binding.CL.setOnClickListener {
            openCustomeDiolog(position)
        }
    }

    override fun getItemCount(): Int {
        return cartFoodList.size
    }

    class ViewHolder(var binding: ItemCartFoodLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}