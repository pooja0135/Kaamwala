package com.project.kaamwaala.activity.customer.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.network.Coroutines

class CustomerCategoryListVM(
    private val repository: CustomerCategoryListRepo
) : ViewModel() {
    var responseCategory = MutableLiveData<CustomerStallCategoryResponse>()

    fun getCategory(request: CustomerStallCategoryRequest) {
        Coroutines.main {
            responseCategory.postValue(repository.GetCategory(request))
        }
    }




}