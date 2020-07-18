package com.project.kaamwaala.fragment.admin.adminorder

import com.google.gson.Gson
import com.project.kaamwaala.model.admin_order.AdminOrderDetailRequest
import com.project.kaamwaala.model.admin_order.AdminOrderDetailResponse
import com.project.kaamwaala.model.admin_order.AdminOrderListRequest
import com.project.kaamwaala.model.admin_order.AdminOrderListResponse
import com.project.kaamwaala.model.admin_stall_detail.*
import com.project.kaamwaala.model.adminstalllist.AdminStallListRequest
import com.project.kaamwaala.model.adminstalllist.AdminStallListResponse
import com.project.kaamwaala.model.category.CategoryListResponse
import com.project.kaamwaala.model.city.CityResponse
import com.project.kaamwaala.model.createstall.CreateStallRequest
import com.project.kaamwaala.model.createstall.CreateStallResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.network.MyApi
import com.project.kaamwaala.network.SafeApiRequest

class AdminOrderRepo(
    private val api: MyApi
) : SafeApiRequest() {


    suspend fun AdminOrderList(request: AdminOrderListRequest): AdminOrderListResponse {
        var response: AdminOrderListResponse? = AdminOrderListResponse(
            "",
            false, emptyList()
        )
        try {
            val jsonObject = Gson().toJson(request, AdminOrderListRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.adminorderlist(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }


    suspend fun AdminOrderDetail(request: AdminOrderDetailRequest): AdminOrderDetailResponse {
        var response: AdminOrderDetailResponse? = AdminOrderDetailResponse(
            "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "",
            false,
            "", "", "", "", "", "", emptyList(), ""
            , ""
        )
        try {
            val jsonObject = Gson().toJson(request, AdminOrderDetailRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.adminorderdetail(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }


}