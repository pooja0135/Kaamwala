package com.project.kaamwaala.activity.customer.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kaamwaala.model.customer.cart.*
import com.project.kaamwaala.model.customer.customer_product.CustomerProductRequest
import com.project.kaamwaala.model.customer.customer_product.CustomerProductResponse
import com.project.kaamwaala.model.customer.order.*
import com.project.kaamwaala.model.stall_category_product.StoreProductDetailRequest
import com.project.kaamwaala.model.stall_category_product.StoreProductDetailResponse
import com.project.kaamwaala.network.Coroutines

class CustomerProductListVM(
    private val repository: CustomerProductListRepo
) : ViewModel() {
    var responseProduct = MutableLiveData<CustomerProductResponse>()
    var responseProductDetail = MutableLiveData<StoreProductDetailResponse>()
    var responseAddCart = MutableLiveData<AddCartResponse>()
    var responseGetCart = MutableLiveData<GetCartResponse>()
    var responseAddAddress = MutableLiveData<AddAddressResponse>()
    var responseUpdatePhone = MutableLiveData<UpdateProfileResponse>()
    var responseAddress = MutableLiveData<GetAddressResponse>()
    var responsePlaceOrder = MutableLiveData<PlaceOrderResponse>()
    var responseOrderList = MutableLiveData<CustomerOrderListResponse>()
    var responseOrderDetail = MutableLiveData<CustomerOrderDetailResponse>()
    var responseCancelList = MutableLiveData<CancelListResponse>()
    var responseCancelOrder = MutableLiveData<CancelResponse>()
    fun getProduct(request: CustomerProductRequest) {
        Coroutines.main {
            responseProduct.postValue(repository.GetProductList(request))
        }
    }
    fun productdetail(request: StoreProductDetailRequest) {
        Coroutines.main {
            responseProductDetail.postValue(repository.ProductDetail(request))
        }
    }

    fun addcart(request: AddCartRequest) {
        Coroutines.main {
            responseAddCart.postValue(repository.AddCart(request))
        }
    }

    fun getcart(request: GetCartRequest) {
        Coroutines.main {
            responseGetCart.postValue(repository.GetCart(request))
        }
    }

    fun addaddress(request: AddAddressRequest) {
        Coroutines.main {
            responseAddAddress.postValue(repository.AddAddress(request))
        }
    }

    fun addphone(request: UpdateProfileRequest) {
        Coroutines.main {
            responseUpdatePhone.postValue(repository.AddPhoneNumber(request))
        }
    }

    fun getaddress(request: GetAddressRequest) {
        Coroutines.main {
            responseAddress.postValue(repository.GetAddress(request))
        }
    }

    fun placeorder(request: PlaceOrderRequest) {
        Coroutines.main {
            responsePlaceOrder.postValue(repository.PlaceOrder(request))
        }
    }

    fun orderlist(request: CustomerOrderListRequest) {
        Coroutines.main {
            responseOrderList.postValue(repository.OrderList(request))
        }
    }


    fun orderdetail(request: CustomerOrderDetailRequest) {
        Coroutines.main {
            responseOrderDetail.postValue(repository.OrderDetail(request))
        }
    }



    fun cancellist() {
        Coroutines.main {
            responseCancelList.postValue(repository.CancelList())
        }
    }

    fun cancelorder(request: CancelRequest) {
        Coroutines.main {
            responseCancelOrder.postValue(repository.CancelOrder(request))
        }
    }

}