package com.project.kaamwaala.fragment.admin.adminorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


@Suppress("UNCHECKED_CAST")
class AdminOrderVMF (
    private val repository: AdminOrderRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AdminOrderVM(repository) as T
    }
}