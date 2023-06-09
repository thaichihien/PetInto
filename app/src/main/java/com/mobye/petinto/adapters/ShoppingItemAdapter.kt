package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.ShoppingItemListBinding
import com.mobye.petinto.models.Product

class ShoppingItemAdapter(
    private val detailListener: (Product) -> Unit,
    private val addListener: (Product, Int) -> Unit
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>() {

    private lateinit var binding: ShoppingItemListBinding
    private val differCallBack = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
    var differ = AsyncListDiffer(this,differCallBack)
    inner class ShoppingItemViewHolder()
        : RecyclerView.ViewHolder(binding.root){
            fun setData(item : Product){
                binding.apply {
                    tvShoppingItemName.text = item.name
                    tvShoppingItemPrice.text =  "%,d đ".format(item.price)
                    tvAnimalType.text = item.typePet
                    tvDetails.text = item.detail
                    tvStock.text = item.stock.toString()
                    Glide.with(binding.root)
                        .load(item.image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(ivShoppingItem)
                    ivShoppingItem.setOnClickListener{
                        detailListener(item)
                    }
                    tvShoppingItemName.setOnClickListener {
                        detailListener(item)
                    }
                    btnPlusShopping.setOnClickListener{
                        var quantity : Int  = tvShoppingItemQuantity.text.toString().toInt()
                        if(quantity < item.stock){
                            quantity += 1
                            tvShoppingItemQuantity.text = quantity.toString()
                        }
                    }
                    btnMinusShopping.setOnClickListener{
                        var quantity : Int  = tvShoppingItemQuantity.text.toString().toInt()
                        if(quantity > 1){
                            quantity -= 1
                            tvShoppingItemQuantity.text = quantity.toString()
                        }
                    }

                    btnAddToCart.setOnClickListener {
                        val quantity : Int  = tvShoppingItemQuantity.text.toString().toInt()
                        addListener(item,quantity)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        binding = ShoppingItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ShoppingItemViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size
    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }
}