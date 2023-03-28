package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.mobye.petinto.databinding.OrderItemListBinding
import com.mobye.petinto.models.PetInfo

class OrderAdapter() : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>(){

    private lateinit var binding: OrderItemListBinding

    private val differCallback = object : DiffUtil.ItemCallback<PetInfo>(){
        override fun areItemsTheSame(oldItem: PetInfo, newItem: PetInfo): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PetInfo, newItem: PetInfo): Boolean {
            return oldItem.id == newItem.id
        }

    }

    var differ = AsyncListDiffer(this, differCallback)

    private val binderHelper : ViewBinderHelper by lazy {
        val setting = ViewBinderHelper()
        setting.setOpenOnlyOne(true)
        setting
    }

    inner class OrderViewHolder : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        binding = OrderItemListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return OrderViewHolder()
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val pet = differ.currentList[position]
        binding.apply {
            Glide.with(binding.root)
                .load(pet.image)
                .into(binding.imgviewOrder)
            tvName.text = pet.name
            tvCondition.text = pet.condition
            tvId.text = pet.id
            tvPrice.text = pet.price.toString()
        }
        holder.setIsRecyclable(false)
//        binderHelper.bind(binding.itemOrderLayout,differ.currentList[position].id)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}