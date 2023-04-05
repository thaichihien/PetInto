package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobye.petinto.databinding.ItemCartListBinding
import com.mobye.petinto.databinding.ItemDeliveryAddressBinding
import com.mobye.petinto.databinding.ShoppingItemListBinding
import com.mobye.petinto.models.DeliveryInfo
import com.mobye.petinto.models.Product
import io.realm.kotlin.types.RealmUUID

class DeliveryInfoAdapter(
    val chooseListener : (Int) -> Unit,
    val unChooseListener : (Int) -> Unit,
    val editListener : (DeliveryInfo) -> Unit
) :  RecyclerView.Adapter<DeliveryInfoAdapter.DeliveryInfoViewHolder>(){
    private lateinit var binding: ItemDeliveryAddressBinding
    private val differCallBack = object : DiffUtil.ItemCallback<DeliveryInfo>(){
        override fun areItemsTheSame(oldItem: DeliveryInfo, newItem: DeliveryInfo): Boolean
            = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DeliveryInfo, newItem: DeliveryInfo): Boolean
            = oldItem.address == newItem.address &&
                oldItem.isDefault == newItem.isDefault

    }
    var differ = AsyncListDiffer(this,differCallBack)
    private var selected = -1
    private var lastSelected = -1

    inner class DeliveryInfoViewHolder() : RecyclerView.ViewHolder(binding.root){
    }

    private fun formatCustomerInfo(deliveryInfo: DeliveryInfo)
        = "${deliveryInfo.name} | ${deliveryInfo.phone}"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryInfoViewHolder {
        binding = ItemDeliveryAddressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return DeliveryInfoViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: DeliveryInfoViewHolder, position: Int) {
        val deliveryInfo = differ.currentList[position]


        if(deliveryInfo.isDefault && selected < 0){
            selected = holder.absoluteAdapterPosition
        }

        binding.apply {
            tvAddress.text = deliveryInfo.address
            tvAddress.setOnClickListener{
                editListener(deliveryInfo)
            }
            rbDeliveryAddress.apply {
                text = formatCustomerInfo(deliveryInfo)
                isChecked = selected == holder.absoluteAdapterPosition

                setOnCheckedChangeListener { _,isChecked ->

                    if(isChecked){
                        selected = holder.absoluteAdapterPosition
                        chooseListener(holder.absoluteAdapterPosition)
                    }


                }
            }
        }

        holder.setIsRecyclable(false)

    }
}