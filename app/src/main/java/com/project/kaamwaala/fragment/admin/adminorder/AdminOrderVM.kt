package com.project.kaamwaala.fragment.admin.adminorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.network.Coroutines

class AdminOrderVM(
    private val repository: AdminOrderRepo
) : ViewModel() {
    var responseOrderList = MutableLiveData<AdminOrderListResponse>()
    var responseOrderDetail = MutableLiveData<AdminOrderDetailResponse>()


    fun adminorderlist(request: AdminOrderListRequest) {
        Coroutines.main {
            responseOrderList.postValue(repository.AdminOrderList(request))
        }
    }

    fun adminorderdetail(request: AdminOrderDetailRequest) {
        Coroutines.main {
            responseOrderDetail.postValue(repository.AdminOrderDetail(request))
        }
    }


}