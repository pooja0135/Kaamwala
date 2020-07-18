package com.project.kaamwaala.network


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.project.kaamwaala.AppController
import com.project.kaamwaala.model.admin_order.AdminOrderDetailResponse
import com.project.kaamwaala.model.admin_order.AdminOrderListResponse
import com.project.kaamwaala.model.admin_stall_detail.*
import com.project.kaamwaala.model.adminstalllist.AdminStallListResponse
import com.project.kaamwaala.model.category.CategoryListResponse
import com.project.kaamwaala.model.city.CityResponse
import com.project.kaamwaala.model.createstall.CreateStallResponse
import com.project.kaamwaala.model.createstall.StoreCategoryResponse
import com.project.kaamwaala.model.customer.cart.*
import com.project.kaamwaala.model.customer.customer_category.CustomerStallCategoryResponse
import com.project.kaamwaala.model.customer.customer_product.CustomerProductResponse
import com.project.kaamwaala.model.customer.customer_store.CustomerStoreResponse
import com.project.kaamwaala.model.customer.dashboard.DashboardResponse
import com.project.kaamwaala.model.customer.order.*
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.model.login.UpdateloginResponse
import com.project.kaamwaala.model.stall_category_product.*
import com.project.kaamwaala.model.stallcategory.CreateCategoryResponse
import okhttp3.RequestBody
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface MyApi {

    @POST("Service/MemberRegistration")
    suspend fun login(
        @Body parameter: RequestBody
    ): Response<LoginResponse>

    @POST("Service/UpdateStoreDetails")
    suspend fun updatestore(
        @Body parameter: RequestBody
    ): Response<CreateStallResponse>


    @POST("Service/StoreList")
    suspend fun adminstorelist(
        @Body parameter: RequestBody
    ): Response<AdminStallListResponse>


    @POST("Service/getStoreDetailsById")
    suspend fun adminstoredetail(
        @Body parameter: RequestBody
    ): Response<AdminStallDetailResponse>


    @POST("Service/FreeShippingAmount")
    suspend fun freeshippingamount(
        @Body parameter: RequestBody
    ): Response<FreeShippingResponse>

    @POST("Service/MinOrderValue")
    suspend fun minordervalue(
        @Body parameter: RequestBody
    ): Response<MinOrderResponse>


    @POST("Service/ShippingCharges")
    suspend fun shippingcharges(
        @Body parameter: RequestBody
    ): Response<ShippingChargesResponse>

    @POST("Service/TimeSlot")
    suspend fun timeslot(
        @Body parameter: RequestBody
    ): Response<TimeSlotResponse>


    @GET("Service/CategoryList")
       suspend fun getsession(
       ): Response<CategoryListResponse>

    @POST("Service/InsertUpdateStoreCategory")
    suspend fun createcategory(
        @Body parameter: RequestBody
    ): Response<CreateCategoryResponse>

    @POST("Service/StoreCategoryList")
    suspend fun storecategorylist(
        @Body parameter: RequestBody
    ): Response<StoreCategoryResponse>


    @POST("Service/UpdateLogin")
    suspend fun updatelogin(
        @Body parameter: RequestBody
    ): Response<UpdateloginResponse>


    @POST("Service/InsertUpdateProduct")
    suspend fun createproduct(
        @Body parameter: RequestBody
    ): Response<CreateProductResponse>


    @POST("Service/InsertUpdateProductImages")
    suspend fun insertproductimage(
        @Body parameter: RequestBody
    ): Response<InsertProductImageResponse>

    @POST("Service/InsertUpdateProductPrice")
    suspend fun insertproductprice(
        @Body parameter: RequestBody
    ): Response<InsertProductPriceResponse>

    @POST("Service/ProductList")
    suspend fun stallproductlist(
        @Body parameter: RequestBody
    ): Response<StoreProductListResponse>

    @POST("Service/getProductDetailsById")
    suspend fun stallproductdetail(
        @Body parameter: RequestBody
    ): Response<StoreProductDetailResponse>

    @GET("Service/CityResponse")
    suspend fun getCity(
    ): Response<CityResponse>


    @POST("Service/CustomerStoreList")
    suspend fun customerstalllist(
        @Body parameter: RequestBody
    ): Response<CustomerStoreResponse>

    @POST("Service/CustomerDashboardList")
    suspend fun dashboardlist(
        @Body parameter: RequestBody
    ): Response<DashboardResponse>


    @POST("Service/CustomerStoreDetailsById")
    suspend fun customercategorylist(
        @Body parameter: RequestBody
    ): Response<CustomerStallCategoryResponse>

    @POST("Service/CustomerProductList")
    suspend fun customerproductlist(
        @Body parameter: RequestBody
    ): Response<CustomerProductResponse>


    @POST("Service/InsertUpdateCart")
    suspend fun addcart(
        @Body parameter: RequestBody
    ): Response<AddCartResponse>

    @POST("Service/ViewCart")
    suspend fun getcart(
        @Body parameter: RequestBody
    ): Response<GetCartResponse>




    @POST("Service/InsertUpdateCustomerAddress")
    suspend fun addaddress(
        @Body parameter: RequestBody
    ): Response<AddAddressResponse>



    @POST("Service/UpdateCustomerDetails")
    suspend fun updatephone(
        @Body parameter: RequestBody
    ): Response<UpdateProfileResponse>

    @POST("Service/CustomerAddressList")
    suspend fun getAddress(
        @Body parameter: RequestBody
    ): Response<GetAddressResponse>


    @POST("Service/PlaceOrder")
    suspend fun placeorder(
        @Body parameter: RequestBody
    ): Response<PlaceOrderResponse>

    @POST("Service/CustomerOrderList")
    suspend fun customerorderlist(
        @Body parameter: RequestBody
    ): Response<CustomerOrderListResponse>

    @POST("Service/CustomerOrderDetails")
    suspend fun customerorderdetail(
        @Body parameter: RequestBody
    ): Response<CustomerOrderDetailResponse>


    @GET("Service/CancelReasonList")
    suspend fun getcancellist(
    ): Response<CancelListResponse>


    @POST("Service/CustomerCancelOrder")
    suspend fun customerordercancel(
        @Body parameter: RequestBody
    ): Response<CancelResponse>


    @POST("Service/StoreOrderList")
    suspend fun adminorderlist(
        @Body parameter: RequestBody
    ): Response<AdminOrderListResponse>

    @POST("Service/StoreOrderDetails")
    suspend fun adminorderdetail(
        @Body parameter: RequestBody
    ): Response<AdminOrderDetailResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {

            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(logInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(AppController.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }


}