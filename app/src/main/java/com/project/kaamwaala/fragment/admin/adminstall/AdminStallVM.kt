package com.project.kaamwaala.fragment.admin.adminstall

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kaamwaala.model.admin_stall_detail.*
import com.project.kaamwaala.model.adminstalllist.AdminStallListRequest
import com.project.kaamwaala.model.adminstalllist.AdminStallListResponse
import com.project.kaamwaala.model.category.CategoryListResponse
import com.project.kaamwaala.model.city.CityResponse
import com.project.kaamwaala.model.createstall.CreateStallRequest
import com.project.kaamwaala.model.createstall.CreateStallResponse
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.network.Coroutines

class AdminStallVM(
    private val repository: AdminStallRepo
) : ViewModel() {
    var responseStall = MutableLiveData<CreateStallResponse>()
    var responseStallList = MutableLiveData<AdminStallListResponse>()
    var responseStallDetail = MutableLiveData<AdminStallDetailResponse>()
    var responseMinOrder = MutableLiveData<MinOrderResponse>()
    var responseFreeShipping = MutableLiveData<FreeShippingResponse>()
    var responseShipping = MutableLiveData<ShippingChargesResponse>()
    var responseTimeSlot = MutableLiveData<TimeSlotResponse>()
    var responseCategory = MutableLiveData<CategoryListResponse>()
    var responseCity = MutableLiveData<CityResponse>()

    fun createstall(request: CreateStallRequest) {
        Coroutines.main {
            responseStall.postValue(repository.CreateStall(request))
        }
    }
    fun stalllist(request: AdminStallListRequest) {
        Coroutines.main {
            responseStallList.postValue(repository.AdminStallList(request))
        }
    }

    fun stalldetail(request: AdminStallDetailRequest) {
        Coroutines.main {
            responseStallDetail.postValue(repository.AdminStallDetail(request))
        }
    }

    fun minorder(request: MinOrderRequest) {
        Coroutines.main {
            responseMinOrder.postValue(repository.MinOrderValue(request))
        }
    }

    fun freeshipping(request: FreeShippingRequest) {
        Coroutines.main {
            responseFreeShipping.postValue(repository.FreeShippingValue(request))
        }
    }
    fun shippingvalue(request: ShippingChargesRequest) {
        Coroutines.main {
            responseShipping.postValue(repository.ShippingValue(request))
        }
    }

    fun timeslotvalue(request: TimeSlotRequest) {
        Coroutines.main {
            responseTimeSlot.postValue(repository.TimeSlotValue(request))
        }
    }

    fun getCategory() {
        Coroutines.main {
            responseCategory.postValue(repository.CategoryList())
        }
    }

    fun getCity() {
        Coroutines.main {
            responseCity.postValue(repository.CityList())
        }
    }

}