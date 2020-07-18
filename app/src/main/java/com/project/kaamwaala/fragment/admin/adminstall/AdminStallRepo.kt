package com.project.kaamwaala.fragment.admin.adminstall

import com.google.gson.Gson
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

class AdminStallRepo(
    private val api: MyApi
) : SafeApiRequest() {


    suspend fun CreateStall(request: CreateStallRequest): CreateStallResponse {
        var response: CreateStallResponse? = CreateStallResponse(
            "",
            false
        )
        try {
            val jsonObject = Gson().toJson(request, CreateStallRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.updatestore(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun AdminStallList(request: AdminStallListRequest): AdminStallListResponse {
        var response: AdminStallListResponse? = AdminStallListResponse(
            "",
            false, emptyList()
        )
        try {
            val jsonObject = Gson().toJson(request, AdminStallListRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.adminstorelist(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun AdminStallDetail(request:AdminStallDetailRequest): AdminStallDetailResponse {
        var response: AdminStallDetailResponse? = AdminStallDetailResponse(
            "","","","","","","","","," ,
                    "","","","","", emptyList() ,
                    "","","","","","","","","","","",
            "", emptyList(), "","","", emptyList(),"",
            "","",false,"","","","", emptyList() ,
                    "","","","")


        try {
            val jsonObject = Gson().toJson(request, AdminStallDetailRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.adminstoredetail(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun MinOrderValue(request:MinOrderRequest): MinOrderResponse {
        var response: MinOrderResponse? = MinOrderResponse(
            "",false,"")
        try {
            val jsonObject = Gson().toJson(request, MinOrderRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.minordervalue(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun FreeShippingValue(request:FreeShippingRequest): FreeShippingResponse {
        var response: FreeShippingResponse? = FreeShippingResponse(
            "",false,"")
        try {
            val jsonObject = Gson().toJson(request, FreeShippingRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.freeshippingamount(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun ShippingValue(request:ShippingChargesRequest): ShippingChargesResponse {
        var response: ShippingChargesResponse? = ShippingChargesResponse(
            "",false,"")
        try {
            val jsonObject = Gson().toJson(request, ShippingChargesRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.shippingcharges(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun TimeSlotValue(request:TimeSlotRequest): TimeSlotResponse {
        var response: TimeSlotResponse? = TimeSlotResponse(
            "",false,"")
        try {
            val jsonObject = Gson().toJson(request, TimeSlotRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.timeslot(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun CategoryList(): CategoryListResponse {
        var response: CategoryListResponse? = CategoryListResponse(
            emptyList(),"",
            false
        )
        try {
            response = apiRequest { api.getsession() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun CityList(): CityResponse {
        var response: CityResponse? = CityResponse(
            emptyList(),"",
            false
        )
        try {
            response = apiRequest { api.getCity() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }


}