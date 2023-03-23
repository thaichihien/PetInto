package com.mobye.petinto.adapters

import android.nfc.NfcAdapter.OnTagRemovedListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemSelectedListener
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
    private val removedListener: (CartItem,Int) -> Unit,
    private val addQuantityListener: (Int) -> Unit,
    private val removeQuantityListener: (Int) -> Unit,
    private val selectedListener: (Boolean,Int) -> Unit
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

    private var isSelectedAll = false


    inner class CartItemViewHolder : RecyclerView.ViewHolder(binding.root){
//        fun setData(cartItem : CartItem,index: Int){
//
//            //TODO set listener for check box
//
//
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        binding = ItemCartSwipeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return CartItemViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        //holder.setData(,holder.adapterPosition)
        val cartItem = differ.currentList[position]
        binding.apply {
            tvItemNameCart.text = cartItem.item.name
            tvItemStockCart.text = cartItem.item.stock.toString()
            tvItemPriceCart.text = "%,d đ".format(cartItem.item.price)
            tvItemTypeCart.text = cartItem.item.type
            tvItemDetailCart.text = cartItem.item.detail
            tvItemQuantityCart.text = cartItem.quantity.toString()
            Glide.with(binding.root)
                .load(cartItem.item.image)
                .into(binding.ivItemCart)

            Log.e("SELECTED_CART","${holder.adapterPosition} : $isSelectedAll")
            cbSelectedCart.isChecked = isSelectedAll

            //selectedListener(isSelectedAll,holder.adapterPosition)

            deleteLayout.setOnClickListener{
                //swipeLayout.close(true)
                swipeLayout.visibility = View.GONE
                removedListener(cartItem,holder.adapterPosition)
            }
            btnMinusCart.setOnClickListener {
                var quantity = tvItemQuantityCart.text.toString().toInt()
                if(quantity > 1){
                    quantity -= 1
                    tvItemQuantityCart.text = quantity.toString()
                }

                removeQuantityListener(holder.adapterPosition)
            }
            btnPlusCart.setOnClickListener {
                var quantity = tvItemQuantityCart.text.toString().toInt()
                quantity += 1
                tvItemQuantityCart.text = quantity.toString()

                addQuantityListener(holder.adapterPosition)
            }
            cbSelectedCart.setOnCheckedChangeListener{ _,isChecked ->
                if(holder.adapterPosition > 0){
                    selectedListener(isChecked,holder.adapterPosition)
                }

            }


        }
        //holder.setIsRecyclable(false)
        binderHelper.bind(binding.swipeLayout,differ.currentList[position].item.id)

    }

    fun selectedAll(yes: Boolean){
        isSelectedAll = yes
        notifyDataSetChanged()
    }


}