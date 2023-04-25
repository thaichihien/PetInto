package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mobye.petinto.databinding.ItemCartListBinding
import com.mobye.petinto.models.CartItem
import com.mobye.petinto.utils.Utils

class PaymentItemAdapter : RecyclerView.Adapter<PaymentItemAdapter.PaymentItemViewHolder>() {

    private lateinit var binding: ItemCartListBinding
    private val differCallBack = object : DiffUtil.ItemCallback<CartItem>(){
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.item!!.id == newItem.item!!.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }

    }
    var differ = AsyncListDiffer(this,differCallBack)

    inner class  PaymentItemViewHolder : ViewHolder(binding.root){
        fun setData(cartItem: CartItem){
            binding.apply {
                Glide.with(binding.root)
                    .load(cartItem.item!!.image)
                    .into(ivItemCart)
                tvItemNameCart.text = cartItem.item!!.name
                tvItemPriceCart.text = Utils.formatMoneyVND(cartItem.item!!.price)
                tvItemTypeCart.text = cartItem.item!!.typePet
                tvItemQuantityCart.text = cartItem.quantity.toString()
                tvItemDetailCart.text = cartItem.item!!.detail
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentItemViewHolder {
        binding = ItemCartListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return PaymentItemViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: PaymentItemViewHolder, position: Int) {
        val cartItem = differ.currentList[position]
        holder.setData(cartItem)
        holder.setIsRecyclable(false)
    }


}