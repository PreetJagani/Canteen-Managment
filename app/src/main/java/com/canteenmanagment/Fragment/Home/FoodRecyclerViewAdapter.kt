package com.canteenmanagment.Fragment.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.databinding.ItemFoodLayout2Binding
import com.canteenmanagment.databinding.ItemFoodLayout3Binding

class FoodRecyclerViewAdapter(val foodList : List<Food>, val openCartDialog : (position : Int) -> Unit) : RecyclerView.Adapter<FoodRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodLayout3Binding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.TVTitle.text = foodList[position].name
        holder.binding.TVPrice.text = "${foodList[position].price.toString()} Rs."

        Glide.with(holder.binding.root)
            .load(foodList[position].imageurl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.drawable.error_image)
            .error(R.drawable.error_image)
            .into(holder.binding.IMFoodImage)

        holder.binding.CL.setOnClickListener {
            openCartDialog(position)
        }

    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    class ViewHolder(var binding: ItemFoodLayout3Binding) : RecyclerView.ViewHolder(binding.root)


}