package com.mobye.petinto.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.models.Product

class ProductPagingSource(
    private val query : String,
    private val  min : String = "",
    private val  max : String = "",
    private val type : String = ""
) : PagingSource<Int, Product>() {
    companion object{
        const val FIRST_PAGE = 1
    }
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE
            val response = RetrofitInstance.api.getProducts(nextPage,query,min,max,type)
            val responseApi = response.body()!!

            LoadResult.Page(
                data = responseApi.body!!,
                prevKey = if(nextPage == FIRST_PAGE) null else nextPage - 1,
                nextKey = if(responseApi.body!!.isEmpty()) null else nextPage + 1
            )

        }catch (e: Exception){
            Log.e("ProductPagingSource",e.toString())
            LoadResult.Error(e)
        }
    }
}