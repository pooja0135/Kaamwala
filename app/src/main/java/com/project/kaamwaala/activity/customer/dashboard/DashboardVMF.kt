package com.project.kaamwaala.activity.customer.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


@Suppress("UNCHECKED_CAST")
class DashboardVMF (
    private val repository: DashboardRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardVM(repository) as T
    }
}