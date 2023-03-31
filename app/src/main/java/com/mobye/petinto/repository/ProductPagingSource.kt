package com.mobye.petinto.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.models.Product

class ProductPagingSource : PagingSource<Int, Product>() {
    companion object{
        const val FIRST_PAGE = 1
    }


    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE
            val response = RetrofitInstance.api.getProducts(nextPage)

            LoadResult.Page(
                data = response.body()!!,
                prevKey = if(nextPage == FIRST_PAGE) null else nextPage - 1,
                nextKey = if(response.body()!!.isEmpty()) null else nextPage + 1
            )

        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}