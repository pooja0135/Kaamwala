package com.project.kaamwaala.activity.customer.category

import com.google.gson.Gson
import com.project.kaamwaala.model.admin_stall_detail.*
import com.project.kaamwaala.model.adminstalllist.AdminStallListRequest
import com.project.kaamwaala.model.adminstalllist.AdminStallListResponse
import com.project.kaamwaala.model.category.CategoryListResponse
import com.project.kaamwaala.model.city.CityResponse
import com.project.kaamwaala.model.createstall.CreateStallRequest
import com.project.kaamwaala.model.createstall.CreateStallResponse
import com.project.kaamwaala.model.customer.customer_category.CustomerStallCategoryRequest
import com.project.kaamwaala.model.customer.customer_category.CustomerStallCategoryResponse
import com.project.kaamwaala.model.customer.customer_store.CustomerStoreRequest
import com.project.kaamwaala.model.customer.customer_store.CustomerStoreResponse
import com.project.kaamwaala.model.customer.dashboard.DashboardRequest
import com.project.kaamwaala.model.customer.dashboard.DashboardResponse
import com.project.kaamwaala.model.customer.order.CancelListResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.network.MyApi
import com.project.kaamwaala.network.SafeApiRequest

class CustomerCategoryListRepo(
    private val api: MyApi
) : SafeApiRequest() {


    suspend fun GetCategory(request: CustomerStallCategoryRequest): CustomerStallCategoryResponse {
        var response: CustomerStallCategoryResponse? = CustomerStallCategoryResponse(
            "","","","","","","","","","",""
            ,"","","","","","","","","","","","",""
            ,"","","","","","",false, emptyList(),"","","","","","","",""
        )
        try {
            val jsonObject = Gson().toJson(request, CustomerStallCategoryRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.customercategorylist(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }








}