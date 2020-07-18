package com.project.kaamwaala.activity.customer.category

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.customer.product.CartListActivity
import com.project.kaamwaala.activity.customer.product.CustomerProductListVM
import com.project.kaamwaala.activity.customer.product.CustomerProductListVMF
import com.project.kaamwaala.adapter.StallCategoryListAdapter
import com.project.kaamwaala.model.customer.cart.GetCartRequest
import com.project.kaamwaala.model.customer.cart.GetCartResponse
import com.project.kaamwaala.model.customer.customer_category.CustomerStallCategoryRequest
import com.project.kaamwaala.model.customer.customer_category.CustomerStallCategoryResponse
import com.project.kaamwaala.model.customer.customer_category.StoreCategoryItem
import com.project.kaamwaala.network.PrefsConstants
import io.paperdb.Paper

import kotlinx.android.synthetic.main.activity_product_category.*
import kotlinx.android.synthetic.main.activity_product_category.progressbar
import kotlinx.android.synthetic.main.activity_product_category.rlCart
import kotlinx.android.synthetic.main.activity_product_category.tvCount
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.inflate_stall_category_list.view.*

import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class ProductCategoryActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: CustomerCategoryListVMF by instance<CustomerCategoryListVMF>()
    private val factory1: CustomerProductListVMF by instance<CustomerProductListVMF>()
    private var viewModel: CustomerCategoryListVM? = null
    private var viewModel1: CustomerProductListVM? = null


    companion object {
        var categorylist: MutableList<StoreCategoryItem> = mutableListOf()
        lateinit var StoreName: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_category)

        Paper.init(this)
        viewModel = ViewModelProviders.of(this, factory).get(CustomerCategoryListVM::class.java)
        viewModel1 = ViewModelProviders.of(this, factory1).get(CustomerProductListVM::class.java)



     //   GetCart(GetCartRequest("Select", Prefs.getString(PrefsConstants.CityId,""),"","",Prefs.getString(PrefsConstants.UserId,""),Prefs.getString(PrefsConstants.StoreId,"")))

        rlCart.setOnClickListener {
            startActivity(Intent(this, CartListActivity::class.java))
        }


    }

    private fun GetCategory(customercategoryRequest: CustomerStallCategoryRequest) {
        viewModel?.responseCategory = MutableLiveData()
        viewModel?.responseCategory?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CustomerStallCategoryResponse)

            categorylist.clear()
            if (response.Status) {

                rvStallCategory.apply {
                    rvStallCategory.layoutManager = GridLayoutManager(context,2)
                    adapter = StallCategoryListAdapter(response.StoreCategoryItem) }


                for (i in 0 until response.StoreCategoryItem.size) {

                    categorylist.add(response.StoreCategoryItem[i])
                }



                Glide.with(this) //1
                    .asBitmap()
                    .load(response.StoreImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .skipMemoryCache(true) //2
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            imageview.setImageBitmap(resource)

                        }
                        override fun onLoadStarted(placeholder: Drawable?) {
                            super.onLoadStarted(placeholder)


                        }
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })

                tvStoreName.setText(response.StoreName)
                tvStore.setText(response.StoreName)
                tvLocation.setText(response.Address)
                tvOwnerName.setText(response.OwnerName)
                tvTerms.setText(response.TermsAndCondition)
                tvRefund.setText(response.RefundPolicy)

                if (response.TermsAndCondition.isEmpty())
                {
                    tvTerms.visibility=View.GONE
                    tvTerms1.visibility=View.GONE
                }

                if (response.RefundPolicy.isEmpty())
                {
                    tvRefund.visibility=View.GONE
                    tvRefund1.visibility=View.GONE
                }



                if(response.IsChat.isEmpty()||response.IsChat.equals("False"))
                {
                    cardviewChat.visibility=View.GONE
                }
                else
                {
                    cardviewChat.visibility=View.VISIBLE
                }


                StoreName=response.StoreName
                progressbar.visibility= View.GONE


            } else {
                progressbar.visibility= View.GONE
                //  activity?.toast("Something went wrong.")
            }
        })

        viewModel?.getCategory(customercategoryRequest)
    }

    private fun GetCart(getCartRequest: GetCartRequest) {
        viewModel1?.responseGetCart = MutableLiveData()
        viewModel1?.responseGetCart?.observe(this, androidx.lifecycle.Observer {

            val response = (it as GetCartResponse)

            Paper.book().delete("cartdetail")
            if (response.Status) {


                Paper.book()
                    .write("cartdetail", response.CartItem)

                tvCount.visibility=View.VISIBLE
                tvCount.setText(response.CartItem.size.toString())

            } else {

                //  activity?.toast("Something went wrong.")
            }

            GetCategory(CustomerStallCategoryRequest("StoreDetails", Prefs.getString(PrefsConstants.CityId,""),"","",Prefs.getString(PrefsConstants.StoreId,"")))

        })

        viewModel1?.getcart(getCartRequest)
    }


    override fun onResume() {
        super.onResume()
        GetCart(GetCartRequest("Select", Prefs.getString(PrefsConstants.CityId,""),"","",Prefs.getString(PrefsConstants.UserId,""),Prefs.getString(PrefsConstants.StoreId,"")))

    }


}
