package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.mobye.petinto.databinding.ItemCartSwipeListBinding
import com.mobye.petinto.models.CartItem

class CartItemAdapter(
    private val removedListener: (CartItem,Int) -> Unit,
    private val addQuantityListener: (Int) -> Unit,
    private val removeQuantityListener: (Int) -> Unit,
    private val selectedListener: (Boolean,Int) -> Unit,
    private val addToTotal: (Int,Int) -> Unit
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    private lateinit var binding: ItemCartSwipeListBinding

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
            //selectedListener(isSelectedAll,holder.adapterPosition)

            deleteLayout.setOnClickListener{
                //swipeLayout.close(true)
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
        binderHelper.bind(binding.swipeLayout,differ.currentList[position].item!!.id)
    }

//    fun selectedAll(yes: Boolean){
//        isSelectedAll = yes
//        notifyDataSetChanged()
//    }


}