package com.project.kaamwaala.activity.customer.product

import com.google.gson.Gson
import com.project.kaamwaala.model.customer.cart.*
import com.project.kaamwaala.model.customer.customer_product.CustomerProductRequest
import com.project.kaamwaala.model.customer.customer_product.CustomerProductResponse
import com.project.kaamwaala.model.customer.order.*
import com.project.kaamwaala.model.stall_category_product.StoreProductDetailRequest
import com.project.kaamwaala.model.stall_category_product.StoreProductDetailResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import com.project.kaamwaala.network.MyApi
import com.project.kaamwaala.network.SafeApiRequest

class CustomerProductListRepo(
    private val api: MyApi
) : SafeApiRequest() {


    suspend fun GetProductList(request: CustomerProductRequest): CustomerProductResponse {
        var response: CustomerProductResponse? = CustomerProductResponse(
            emptyList(),"",false

        )
        try {
            val jsonObject = Gson().toJson(request, CustomerProductRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.customerproductlist(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun ProductDetail(request: StoreProductDetailRequest): StoreProductDetailResponse {
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


    suspend fun AddCart(request: AddCartRequest): AddCartResponse {
        var response: AddCartResponse? = AddCartResponse(
            "","",false)
        try {
            val jsonObject = Gson().toJson(request, AddCartRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.addcart(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }
    suspend fun GetCart(request: GetCartRequest): GetCartResponse {
        var response: GetCartResponse? = GetCartResponse(
            "","","", emptyList(),"","","","","",
        "","","","","","","","","",""
        ,"","","","","","","",""
        ,"","","","","","",false,""
        ,"","","","","","","")
        try {
            val jsonObject = Gson().toJson(request, GetCartRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getcart(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }


    suspend fun AddAddress(request: AddAddressRequest): AddAddressResponse {
        var response: AddAddressResponse? = AddAddressResponse(
            "","",false)
        try {
            val jsonObject = Gson().toJson(request, AddAddressRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.addaddress(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun AddPhoneNumber(request: UpdateProfileRequest): UpdateProfileResponse {
        var response: UpdateProfileResponse? = UpdateProfileResponse(
            "","",false)
        try {
            val jsonObject = Gson().toJson(request, UpdateProfileRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.updatephone(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun GetAddress(request: GetAddressRequest): GetAddressResponse {
        var response: GetAddressResponse? = GetAddressResponse(
            emptyList(),"",false)
        try {
            val jsonObject = Gson().toJson(request, GetAddressRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getAddress(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun PlaceOrder(request: PlaceOrderRequest): PlaceOrderResponse {
        var response: PlaceOrderResponse? = PlaceOrderResponse(
            "","","","","",false)
        try {
            val jsonObject = Gson().toJson(request, PlaceOrderRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.placeorder(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun OrderList(request: CustomerOrderListRequest): CustomerOrderListResponse {
        var response: CustomerOrderListResponse? = CustomerOrderListResponse(
            "", emptyList(),false)
        try {
            val jsonObject = Gson().toJson(request, CustomerOrderListRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.customerorderlist(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun OrderDetail(request: CustomerOrderDetailRequest): CustomerOrderDetailResponse {
        var response: CustomerOrderDetailResponse? = CustomerOrderDetailResponse(
            "","","","","","","" ,"","","","",emptyList(),"","","","",false,"","","","","","","","")
        try {
            val jsonObject = Gson().toJson(request, CustomerOrderDetailRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.customerorderdetail(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun CancelList(): CancelListResponse {
        var response: CancelListResponse? = CancelListResponse(
            emptyList(),"",
            false
        )
        try {
            response = apiRequest { api.getcancellist() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }

    suspend fun CancelOrder(request: CancelRequest): CancelResponse {
        var response: CancelResponse? = CancelResponse(
            "", "",false)
        try {
            val jsonObject = Gson().toJson(request, CancelRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.customerordercancel(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }




}