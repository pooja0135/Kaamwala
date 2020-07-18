package com.project.kaamwaala.fragment.admin.inventory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kaamwaala.model.admin_stall_detail.*
import com.project.kaamwaala.model.adminstalllist.AdminStallListRequest
import com.project.kaamwaala.model.adminstalllist.AdminStallListResponse
import com.project.kaamwaala.model.category.CategoryListResponse
import com.project.kaamwaala.model.createstall.CreateStallRequest
import com.project.kaamwaala.model.createstall.CreateStallResponse
import com.project.kaamwaala.model.createstall.StoreCategoryListRequest
import com.project.kaamwaala.model.createstall.StoreCategoryResponse
import com.project.kaamwaala.model.login.LoginRequest
import com.project.kaamwaala.model.login.LoginResponse
import com.project.kaamwaala.model.stall_category_product.*
import com.project.kaamwaala.model.stallcategory.CreateCategoryRequest
import com.project.kaamwaala.model.stallcategory.CreateCategoryResponse
import com.project.kaamwaala.network.Coroutines

class StallCategoryVM(
    private val repository: StallCategoryRepo
) : ViewModel() {
    var responseCreateCategory = MutableLiveData<CreateCategoryResponse>()
    var responseCategoryList = MutableLiveData<StoreCategoryResponse>()
    var responseCreateProduct = MutableLiveData<CreateProductResponse>()
    var responseProductList = MutableLiveData<StoreProductListResponse>()
    var responseProductDetail = MutableLiveData<StoreProductDetailResponse>()
    var responseInsertProductImage = MutableLiveData<InsertProductImageResponse>()
    var responseInsertProductPrice = MutableLiveData<InsertProductPriceResponse>()


    fun createcategory(request: CreateCategoryRequest) {
        Coroutines.main {
            responseCreateCategory.postValue(repository.CreateCategory(request))
        }
    }

    fun storecategory(request: StoreCategoryListRequest) {
        Coroutines.main {
            responseCategoryList.postValue(repository.CategoryList(request))
        }
    }

    fun createproduct(request: CreateProductRequest) {
        Coroutines.main {
            responseCreateProduct.postValue(repository.CreateProduct(request))
        }
    }

    fun insertproductimage(request: InsertProductImageRequest) {
        Coroutines.main {
            responseInsertProductImage.postValue(repository.InsertProductImage(request))
        }
    }

    fun insertproductprice(request: InsertProductPriceRequest) {
        Coroutines.main {
            responseInsertProductPrice.postValue(repository.InsertProductPrice(request))
        }
    }


    fun productlist(request: StoreProductListRequest) {
        Coroutines.main {
            responseProductList.postValue(repository.ProductList(request))
        }
    }

    fun productdetail(request: StoreProductDetailRequest) {
        Coroutines.main {
            responseProductDetail.postValue(repository.ProductDetail(request))
        }
    }

}