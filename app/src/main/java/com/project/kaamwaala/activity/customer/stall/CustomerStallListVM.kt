package com.project.kaamwaala.activity.customer.stall

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kaamwaala.model.admin_stall_detail.*
import com.project.kaamwaala.model.adminstalllist.AdminStallListRequest
import com.project.kaamwaala.model.adminstalllist.AdminStallListResponse
import com.project.kaamwaala.model.category.CategoryListResponse
import com.project.kaamwaala.model.city.CityResponse
import com.project.kaamwaala.model.createstall.CreateStallRequest
import com.project.kaamwaala.model.createstall.CreateStallResponse
import com.project.kaamwaala.model.customer.customer_store.CustomerStoreRequest
import com.project.kaamwaala.model.customer.customer_store.CustomerStoreResponse
import com.project.kaamwaala.model.customer.dashboard.DashboardRequest
import com.project.kaamwaala.model.customer.dashboard.DashboardResponse
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.network.Coroutines

class CustomerStallListVM(
    private val repository: CustomerStallListRepo
) : ViewModel() {
    var responseStall = MutableLiveData<CustomerStoreResponse>()

    fun getStall(request: CustomerStoreRequest) {
        Coroutines.main {
            responseStall.postValue(repository.GetStore(request))
        }
    }




}