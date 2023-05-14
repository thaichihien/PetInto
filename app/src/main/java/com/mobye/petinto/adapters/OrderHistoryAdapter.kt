package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.ItemHistoryInformationThingsBinding
import com.mobye.petinto.models.apimodel.OrderHistory
import com.mobye.petinto.utils.Utils

class OrderHistoryAdapter : RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    private lateinit var binding : ItemHistoryInformationThingsBinding
    private val differCallBack = object : DiffUtil.ItemCallback<OrderHistory>(){
        override fun areItemsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean {
            return oldItem == newItem
        }
    }
    var differ = AsyncListDiffer(this,differCallBack)

    inner class OrderHistoryViewHolder : ViewHolder(binding.root){
        fun setData(orderHistory: OrderHistory){
            binding.apply {
                tvName.text = orderHistory.name
                tvPrice.text = Utils.formatMoneyVND(orderHistory.price)
                tvTypePet.text = orderHistory.typePet
                tvDetail.text = orderHistory.detail
                tvQuantity.text = orderHistory.quantity.toString()
                tvStatus.text = orderHistory.status
                tvDate.text = Utils.formatToLocalDate(orderHistory.date)
                Glide.with(binding.root)
                    .load(orderHistory.image)
                    .placeholder(R.drawable.logo_chat)
                    .into(binding.ivImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        binding = ItemHistoryInformationThingsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return OrderHistoryViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val orderHistory = differ.currentList[position]
        holder.setData(orderHistory)
        holder.setIsRecyclable(false)
    }
}