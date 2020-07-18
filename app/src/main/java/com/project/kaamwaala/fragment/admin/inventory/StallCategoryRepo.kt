package com.project.kaamwaala.fragment.admin.inventory

import com.google.gson.Gson
import com.project.kaamwaala.model.admin_stall_detail.*
import com.project.kaamwaala.model.adminstalllist.AdminStallListRequest
import com.project.kaamwaala.model.adminstalllist.AdminStallListResponse
import com.project.kaamwaala.model.category.CategoryListResponse
import com.project.kaamwaala.model.createstall.CreateStallRequest
import com.project.kaamwaala.model.createstall.CreateStallResponse
import com.project.kaamwaala.model.createstall.StoreCategoryListRequest
import com.project.kaamwaala.model.createstall.StoreCategoryResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.model.stall_category_product.*
import com.project.kaamwaala.model.stallcategory.CreateCategoryRequest
import com.project.kaamwaala.model.stallcategory.CreateCategoryResponse
import com.project.kaamwaala.network.MyApi
import com.project.kaamwaala.network.SafeApiRequest

class StallCategoryRepo(
    private val api: MyApi
) : SafeApiRequest() {


    suspend fun CreateCategory(request:CreateCategoryRequest): CreateCategoryResponse {
        var response: CreateCategoryResponse? = CreateCategoryResponse(
            "",false)
        try {
            val jsonObject = Gson().toJson(request, CreateCategoryRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.createcategory(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }



    suspend fun CategoryList(request:StoreCategoryListRequest): StoreCategoryResponse {
        var response: StoreCategoryResponse? = StoreCategoryResponse(
            "",false, emptyList())
        try {
            val jsonObject = Gson().toJson(request, StoreCategoryListRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.storecategorylist(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }


    suspend fun CreateProduct(request:CreateProductRequest): CreateProductResponse {
        var response: CreateProductResponse? = CreateProductResponse(
            "","",false)
        try {
            val jsonObject = Gson().toJson(request, CreateProductRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.createproduct(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }


    suspend fun InsertProductImage(request:InsertProductImageRequest): InsertProductImageResponse {
        var response: InsertProductImageResponse? = InsertProductImageResponse(
            "","",false)
        try {
            val jsonObject = Gson().toJson(request, InsertProductImageRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.insertproductimage(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun InsertProductPrice(request:InsertProductPriceRequest): InsertProductPriceResponse {
        var response: InsertProductPriceResponse? = InsertProductPriceResponse(
            "","",false)
        try {
            val jsonObject = Gson().toJson(request, InsertProductPriceRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.insertproductprice(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }


    suspend fun ProductList(request:StoreProductListRequest): StoreProductListResponse {
        var response: StoreProductListResponse? = StoreProductListResponse(
            "", emptyList(),false)
        try {
            val jsonObject = Gson().toJson(request, StoreProductListRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.stallproductlist(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun ProductDetail(request:StoreProductDetailRequest): StoreProductDetailResponse {
        var response: StoreProductDetailResponse? = StoreProductDetailResponse(
            "","","","", emptyList(), emptyList(),false)
        try {
            val jsonObject = Gson().toJson(request, StoreProductDetailRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.stallproductdetail(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }


}