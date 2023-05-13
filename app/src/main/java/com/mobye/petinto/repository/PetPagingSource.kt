package com.mobye.petinto.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.models.PetInfo

class PetPagingSource(
    private val query : String,
    private val type: String,
    private val gender : String,
    private val minPrice : String,
    private val maxPrice : String,
    private val minAge : String,
    private val maxAge : String
) : PagingSource<Int, PetInfo>(){

    companion object{
        const val FIRST_PAGE = 1
    }

    override fun getRefreshKey(state: PagingState<Int, PetInfo>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PetInfo> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE
            val response = RetrofitInstance.api.getPets(nextPage,query,type,gender,minPrice,maxPrice,minAge,maxAge)
            val responseApi = response.body()!!

            LoadResult.Page(
                data = responseApi.body!!,
                prevKey = if(nextPage == ProductPagingSource.FIRST_PAGE) null else nextPage - 1,
                nextKey = if(responseApi.body!!.isEmpty()) null else nextPage + 1
            )

        }catch (e: Exception){
            Log.e("PetPagingSource",e.toString())
            LoadResult.Error(e)
        }
    }
}