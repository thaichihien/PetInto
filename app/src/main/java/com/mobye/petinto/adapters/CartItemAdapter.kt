package com.mobye.petinto.adapters

import android.nfc.NfcAdapter.OnTagRemovedListener
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.mobye.petinto.databinding.ItemCartListBinding
import com.mobye.petinto.databinding.ItemCartSwipeListBinding
import com.mobye.petinto.databinding.ShoppingItemListBinding
import com.mobye.petinto.models.CartItem
import com.mobye.petinto.models.ShoppingItem

class CartItemAdapter(
    private val removedListener: (CartItem,Int) -> Unit
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    private lateinit var binding: ItemCartSwipeListBinding
    private val differCallBack = object : DiffUtil.ItemCallback<CartItem>(){
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.item.id == newItem.item.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }

    }
    var differ = AsyncListDiffer(this,differCallBack)
    private val binderHelper : ViewBinderHelper by lazy {
        val setting = ViewBinderHelper()
        setting.setOpenOnlyOne(true)
        setting
    }



    inner class CartItemViewHolder : RecyclerView.ViewHolder(binding.root){
        fun setData(cartItem : CartItem,index: Int){
            binding.apply {
                tvItemNameCart.text = cartItem.item.name
                tvItemStockCart.text = cartItem.item.stock.toString()
                tvItemPriceCart.text = cartItem.item.price.toString()
                tvItemTypeCart.text = cartItem.item.type
                tvItemDetailCart.text = cartItem.item.detail
                tvItemQuantityCart.text = cartItem.quantity.toString()
                Glide.with(binding.root)
                    .load(cartItem.item.image)
                    .into(binding.ivItemCart)

                deleteLayout.setOnClickListener{
                    removedListener(cartItem,index)
                }
            }
            //TODO set listener for check box


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        binding = ItemCartSwipeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return CartItemViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.setData(differ.currentList[position],holder.adapterPosition)
        holder.setIsRecyclable(false)
        binderHelper.bind(binding.swipeLayout,differ.currentList[position].item.id)

    }


}