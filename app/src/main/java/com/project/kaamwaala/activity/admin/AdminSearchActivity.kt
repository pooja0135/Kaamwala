package com.project.kaamwaala.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.admin.Product.AdminProductListAdapter
import com.project.kaamwaala.fragment.admin.inventory.StallCategoryVM
import com.project.kaamwaala.fragment.admin.inventory.StallCategoryVMF
import com.project.kaamwaala.model.stall_category_product.StoreProductListRequest
import com.project.kaamwaala.model.stall_category_product.StoreProductListResponse
import kotlinx.android.synthetic.main.activity_admin_search.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AdminSearchActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: StallCategoryVMF by instance<StallCategoryVMF>()
    private lateinit var VM: StallCategoryVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_search)
        VM = ViewModelProviders.of(this, factory).get(StallCategoryVM::class.java)


        //====================================API=====================================================//

       try{
           ProductDetailApi(StoreProductListRequest("Select","","","","",intent.getStringExtra("store_id"),"0"))

       }
       catch (e:Exception)
       {
           progressbar.visibility= View.GONE

       }


    }

//================API==================================================================//
    private fun ProductDetailApi(storeProductListRequest: StoreProductListRequest) {
        VM?.responseProductList = MutableLiveData()
        VM?.responseProductList?.observe(this, androidx.lifecycle.Observer {

            val response = (it as StoreProductListResponse)
            if (response.Status) {
                recyclerviewSearch.apply {
                    recyclerviewSearch.layoutManager = LinearLayoutManager(context)
                    adapter = AdminProductListAdapter(response.ProductItem)
                }
                progressbar.visibility= View.GONE
            }

            else {
                progressbar.visibility= View.GONE

            }

        })
        VM.productlist(storeProductListRequest)
    }

}