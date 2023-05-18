package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobye.petinto.databinding.ItemServiceBookingListBinding
import com.mobye.petinto.models.apimodel.Booking
import com.mobye.petinto.utils.Utils

class BookingListItemAdapter(
    private val detailListener: (Booking) -> Unit,
    private val cancelListener: (Booking) -> Unit
) : RecyclerView.Adapter<BookingListItemAdapter.BookingViewHolder>() {

    private lateinit var binding: ItemServiceBookingListBinding
    private val differCallBack = object : DiffUtil.ItemCallback<Booking>(){
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
           return oldItem.id == newItem.id &&
                   oldItem.status == newItem.status
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
                if(booking.phone.isNullOrBlank()){
                    customerInformationTV.text = booking.customerName
                }else{

                    customerInformationTV.text = "${booking.customerName} | ${booking.phone}"
                }

                petInformationTV.text = booking.petName
                genreTV.text = booking.genre
                weightTV.text = booking.weight
                tvStatus.text = booking.status

                if(booking.status == "Done" || booking.status == "Cancelled" || booking.status == "Unaccepted") {
                    btnCancel.visibility = View.INVISIBLE
                }else{
                    btnCancel.setOnClickListener{
                        cancelListener(booking)
                    }
                }


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

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }
}