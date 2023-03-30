package com.mobye.petinto.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.ListOwnerPetBinding
import com.mobye.petinto.databinding.ShoppingItemListBinding
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.ShoppingItem

class CarouselAdapter() :
    RecyclerView.Adapter<CarouselAdapter.CarouseItemViewHolder>() {

    private lateinit var binding: ListOwnerPetBinding
    private val differCallBack = object : DiffUtil.ItemCallback<PetInfo>(){
        override fun areItemsTheSame(oldItem: PetInfo, newItem: PetInfo): Boolean {
            return oldItem.name == newItem.name
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PetInfo, newItem: PetInfo): Boolean {
            return oldItem.name == newItem.name
        }
    }

    var differ = AsyncListDiffer(this,differCallBack)

    inner class CarouseItemViewHolder() : RecyclerView.ViewHolder(binding.root){
        fun setData(item : PetInfo){
            binding.apply {
                tvName.text =  item.name
                tvIsVaccinated.text = item.vaccine.toString()
                tvGender.text = item.gender
                tvAge.text = item.age.toString()
                tvType.text = item.type
                tvVariety.text = item.variety
                tvWeight.text = item.weight.toString()
                tvColor.text = item.color

                Glide.with(binding.root)
                    .load(item.image)
                    .placeholder(R.drawable.avatar_1)
                    .into(imgAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouseItemViewHolder {
        binding = ListOwnerPetBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return CarouseItemViewHolder()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CarouseItemViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }


}