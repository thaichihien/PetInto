package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobye.petinto.databinding.BookingItemListBinding
import com.mobye.petinto.databinding.ItemCartListBinding
import com.mobye.petinto.databinding.ItemServiceBookingListBinding
import com.mobye.petinto.models.CartItem
import com.mobye.petinto.models.Product
import com.mobye.petinto.models.apimodel.Booking
import com.mobye.petinto.utils.Utils

class BookingListItemAdapter(
    private val detailListener: (Booking) -> Unit,
) : RecyclerView.Adapter<BookingListItemAdapter.BookingViewHolder>() {

    private lateinit var binding: ItemServiceBookingListBinding
    private val differCallBack = object : DiffUtil.ItemCallback<Booking>(){
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
           return oldItem == newItem
        }


    }
    var differ = AsyncListDiffer(this,differCallBack)
    inner class BookingViewHolder() : ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun setData(booking : Booking){
            binding.apply {
                tvService.text = if(booking.service == "Spa") "Spa Booking" else "Hotel Booking"
                lbService.text = if(booking.service == "Spa") "Service:" else "Room:"
                tvType.text = booking.type
                tvDate.text = Utils.formatToLocalDate(booking.checkIn)
                if(booking.phone.isNotBlank()){
                    customerInformationTV.text = "${booking.customerName}|${booking.phone}"
                }else{
                    customerInformationTV.text = booking.customerName
                }

                petInformationTV.text = booking.petName
                genreTV.text = booking.genre
                weightTV.text = booking.weight
                tvStatus.text = booking.status

                layoutBookingItem.setOnClickListener{
                    detailListener(booking)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        binding = ItemServiceBookingListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return BookingViewHolder()
    }

    override fun getItemCount(): Int =differ.currentList.size

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.setData(item)
        holder.setIsRecyclable(false)
    }
}