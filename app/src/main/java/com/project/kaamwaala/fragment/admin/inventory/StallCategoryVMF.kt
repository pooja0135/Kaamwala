package com.project.kaamwaala.fragment.admin.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


@Suppress("UNCHECKED_CAST")
class StallCategoryVMF (
    private val repository: StallCategoryRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StallCategoryVM(repository) as T
    }
}