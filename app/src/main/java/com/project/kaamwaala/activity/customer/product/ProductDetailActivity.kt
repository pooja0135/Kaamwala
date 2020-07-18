package com.project.kaamwaala.activity.customer.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.OnItemStock
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.admin.ProductImage.ProductDetailImageListAdapter
import com.project.kaamwaala.adapter.admin.customer.ProductDetailPriceListAdapter
import com.project.kaamwaala.model.stall_category_product.StoreProductDetailRequest
import com.project.kaamwaala.model.stall_category_product.StoreProductDetailResponse
import kotlinx.android.synthetic.main.activity_product_detail.*

import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ProductDetailActivity : AppCompatActivity(), KodeinAware, OnItemStock {
    override val kodein by kodein()
    var list: MutableList<String> = mutableListOf()

    private val factory: CustomerProductListVMF by instance<CustomerProductListVMF>()
    private lateinit var VM: CustomerProductListVM
    lateinit var callback1: OnItemStock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)


        VM = ViewModelProviders.of(this, factory).get(CustomerProductListVM::class.java)


        callback1=this


        ivBack.setOnClickListener { finish() }
        ivShare.setOnClickListener {  }


        ProductDetailApi(StoreProductDetailRequest(intent.getStringExtra("productid")))

    }

    private fun ProductDetailApi(storeProductDetailRequest: StoreProductDetailRequest) {
        VM?.responseProductDetail = MutableLiveData()
        VM?.responseProductDetail?.observe(this, androidx.lifecycle.Observer {

            val response = (it as StoreProductDetailResponse)
            if (response.Status) {
               // productid=response.Code
                tvProductName.setText(response.Name)
                tvProductName1.setText(response.Name)
                tvDescription.setText(response.Description)


                rvImage.apply {
                    rvImage.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
                    adapter = ProductDetailImageListAdapter(response.ProductImagesItem) }


                rvPrice.apply {
                    rvPrice.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
                    adapter = ProductDetailPriceListAdapter(response.ProductPriceItem,callback1) }



                progressbar.visibility= View.GONE
            }

            else {
                progressbar.visibility= View.GONE

            }

        })
        VM.productdetail(storeProductDetailRequest)
    }

    override fun onClickStock(id: String, value: String, price: String) {

    }


}