package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.ItemHistoryInformationBinding
import com.mobye.petinto.databinding.ItemHistoryInformationThingsBinding
import com.mobye.petinto.models.apimodel.OrderHistory
import com.mobye.petinto.models.apimodel.PetOrderHistory
import com.mobye.petinto.utils.Utils

class OrderPetHistoryAdapter : RecyclerView.Adapter<OrderPetHistoryAdapter.OrderPetHistoryViewHolder>() {

    private lateinit var binding : ItemHistoryInformationBinding
    private val differCallBack = object : DiffUtil.ItemCallback<PetOrderHistory>(){
        override fun areItemsTheSame(oldItem: PetOrderHistory, newItem: PetOrderHistory): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PetOrderHistory, newItem: PetOrderHistory): Boolean {
            return oldItem == newItem
        }
    }
    var differ = AsyncListDiffer(this,differCallBack)

    inner class OrderPetHistoryViewHolder : RecyclerView.ViewHolder(binding.root){
        fun setData(orderPetHistory: PetOrderHistory){
            binding.apply {
//                tvFullname.text = orderPetHistory.name
//                tvPrice.text = Utils.formatMoneyVND(orderPetHistory.price)
//                tvColor.text = orderPetHistory.color
//                tvStatus.text = orderPetHistory.status
//                tvDateBuy.text = Utils.formatToLocalDate(orderPetHistory.date)
//                Glide.with(binding.root)
//                    .load(orderPetHistory.image)
//                    .placeholder(R.drawable.logo_chat)
//                    .into(binding.iconIV)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderPetHistoryAdapter.OrderPetHistoryViewHolder {
        binding = ItemHistoryInformationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return OrderPetHistoryViewHolder()
    }

    override fun onBindViewHolder(holder: OrderPetHistoryViewHolder, position: Int) {
        val orderPetHistory = differ.currentList[position]
        holder.setData(orderPetHistory)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = differ.currentList.size
}