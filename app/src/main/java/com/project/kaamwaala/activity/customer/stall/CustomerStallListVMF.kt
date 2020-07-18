package com.project.kaamwaala.activity.customer.stall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


@Suppress("UNCHECKED_CAST")
class CustomerStallListVMF (
    private val repository: CustomerStallListRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CustomerStallListVM(repository) as T
    }
}