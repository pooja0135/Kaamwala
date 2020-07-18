package com.project.kaamwaala.activity.customer.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


@Suppress("UNCHECKED_CAST")
class CustomerCategoryListVMF (
    private val repository: CustomerCategoryListRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CustomerCategoryListVM(repository) as T
    }
}