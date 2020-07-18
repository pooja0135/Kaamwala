package com.project.kaamwaala.activity.customer.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider



@Suppress("UNCHECKED_CAST")
class CustomerProductListVMF (
    private val repository: CustomerProductListRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CustomerProductListVM(repository) as T
    }
}