package com.project.kaamwaala.fragment.admin.adminstall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


@Suppress("UNCHECKED_CAST")
class AdminStallVMF (
    private val repository: AdminStallRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AdminStallVM(repository) as T
    }
}