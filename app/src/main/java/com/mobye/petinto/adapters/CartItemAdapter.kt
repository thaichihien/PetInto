package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobye.petinto.databinding.ItemCartListSwipeBinding
import com.mobye.petinto.models.CartItem

class CartItemAdapter(
    private val removedListener: (CartItem,Int) -> Unit,
    private val addQuantityListener: (Int) -> Unit,
    private val removeQuantityListener: (Int) -> Unit,
    private val selectedListener: (Boolean,Int) -> Unit,
    private val addToTotal: (Int,Int) -> Unit
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    private lateinit var binding: ItemCartListSwipeBinding

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

    private var isSelectedAll = false

    inner class CartItemViewHolder : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        binding = ItemCartListSwipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return CartItemViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = differ.currentList[position]


        binding.apply {
            tvItemNameCart.text = cartItem.item!!.name
            tvItemStockCart.text = cartItem.item!!.stock.toString()
            tvItemPriceCart.text = "%,d đ".format(cartItem.item!!.price)
            tvItemTypeCart.text = cartItem.item!!.typePet
            tvItemDetailCart.text = cartItem.item!!.detail
            tvItemQuantityCart.text = cartItem.quantity.toString()
            Glide.with(binding.root)
                .load(cartItem.item!!.image)
                .into(binding.ivItemCart)


            //Log.e("SELECTED_CART","${holder.adapterPosition} : ${cartItem.selected}")
            cbSelectedCart.isChecked = cartItem.selected

            btnDelete.setOnClickListener{
                swipeLayout.visibility = View.GONE
                removedListener(cartItem,holder.absoluteAdapterPosition)
            }
            btnMinusCart.setOnClickListener {
                var quantity = tvItemQuantityCart.text.toString().toInt()
                if(quantity > 1){
                    quantity -= 1
                    tvItemQuantityCart.text = quantity.toString()
                    if(cbSelectedCart.isChecked) addToTotal(holder.absoluteAdapterPosition,-1)
                }
                removeQuantityListener(holder.absoluteAdapterPosition)

            }
            btnPlusCart.setOnClickListener {
                var quantity = tvItemQuantityCart.text.toString().toInt()
               if(quantity < cartItem.item!!.stock){
                   quantity += 1
                   tvItemQuantityCart.text = quantity.toString()
               }

                addQuantityListener(holder.absoluteAdapterPosition)
                if(cbSelectedCart.isChecked) addToTotal(holder.absoluteAdapterPosition,1)
            }
            cbSelectedCart.setOnClickListener(null)
            cbSelectedCart.setOnCheckedChangeListener{ _,isChecked ->
                selectedListener(isChecked,holder.absoluteAdapterPosition)
            }
        }
        holder.setIsRecyclable(false)
    }
}