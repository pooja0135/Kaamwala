package com.project.kaamwaala.activity.customer.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kaamwaala.model.admin_stall_detail.*
import com.project.kaamwaala.model.adminstalllist.AdminStallListRequest
import com.project.kaamwaala.model.adminstalllist.AdminStallListResponse
import com.project.kaamwaala.model.category.CategoryListResponse
import com.project.kaamwaala.model.city.CityResponse
import com.project.kaamwaala.model.createstall.CreateStallRequest
import com.project.kaamwaala.model.createstall.CreateStallResponse
import com.project.kaamwaala.model.customer.dashboard.DashboardRequest
import com.project.kaamwaala.model.customer.dashboard.DashboardResponse
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.network.Coroutines

class DashboardVM(
    private val repository: DashboardRepo
) : ViewModel() {
    var responseDashboard = MutableLiveData<DashboardResponse>()
    var responseCity = MutableLiveData<CityResponse>()
    var responseCategory = MutableLiveData<CategoryListResponse>()

    fun getDashboard(request: DashboardRequest) {
        Coroutines.main {
            responseDashboard.postValue(repository.GetDashboard(request))
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