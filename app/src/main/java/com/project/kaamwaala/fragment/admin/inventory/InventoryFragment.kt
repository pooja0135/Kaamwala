package com.project.kaamwaala.fragment.admin.inventory


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.admin.inventory.InventoryListAdapter
import com.project.kaamwaala.dialog.admin.CreateCategoryDialog
import com.project.kaamwaala.model.admin_stall_detail.AdminStallDetailResponse
import com.project.kaamwaala.model.createstall.StoreCategoryListRequest
import com.project.kaamwaala.model.createstall.StoreCategoryResponse
import com.project.kaamwaala.model.stallcategory.CreateCategoryRequest
import com.project.kaamwaala.model.stallcategory.CreateCategoryResponse
import com.project.kaamwaala.OnItemCreateCategory
import com.project.kaamwaala.activity.admin.AdminSearchActivity
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_inventory.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class InventoryFragment: Fragment(),  KodeinAware, OnItemCreateCategory {
    var list: MutableList<String> = mutableListOf()
    override val kodein by kodein()
    private val factory: StallCategoryVMF by instance<StallCategoryVMF>()
    private lateinit var VM: StallCategoryVM
    lateinit var callback: OnItemCreateCategory
    lateinit var responseValue : AdminStallDetailResponse
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View=inflater.inflate(R.layout.fragment_inventory, container, false)
        VM = ViewModelProviders.of(this, factory).get(StallCategoryVM::class.java)
        return  view

    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        callback=this

        responseValue= Paper.book().read("stalldetail")

        tvAddNew.setOnClickListener { createInventoryDialog() }

        CategoryListApi(StoreCategoryListRequest("Select","","","",responseValue.Store_Id,"0"))


        rlSearch.setOnClickListener{
            startActivity(Intent(activity,AdminSearchActivity::class.java).putExtra("store_id",responseValue.Store_Id))
        }

    }


    private fun createInventoryDialog() {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        val newFragment =
            CreateCategoryDialog()
        newFragment.show(ft, "dialog")
        newFragment.setCallBack(callback)
    }

    override fun onClickCategory(id: String) {
      CreateStallApi(CreateCategoryRequest("Insert",id,"","",responseValue.Store_Id,"0"))
    }


    //==========================================API===================================================//
    private fun CreateStallApi(createCategoryRequest: CreateCategoryRequest) {
        Progressbar.visibility=View.VISIBLE
        VM?.responseCreateCategory = MutableLiveData()
        VM?.responseCreateCategory?.observe(this, Observer {
            Progressbar.visibility=View.GONE
            val response = ( it as CreateCategoryResponse)
            if(response.Status)
            {
                CategoryListApi(StoreCategoryListRequest("Select","","","",responseValue.Store_Id,"0"))
            }
            else
            {

            }

        })
        VM.createcategory(createCategoryRequest)
    }

    private fun CategoryListApi(storeCategoryListRequest: StoreCategoryListRequest) {
        Progressbar.visibility=View.VISIBLE
        VM?.responseCategoryList = MutableLiveData()
        VM?.responseCategoryList?.observe(this, Observer {

            val response = ( it as StoreCategoryResponse)
            if(response.Status)
            {
                rvInventoryList.apply {
                    rvInventoryList.layoutManager = LinearLayoutManager(context)
                    adapter = InventoryListAdapter(response.StoreCategoryItem) }

                Progressbar.visibility=View.GONE
            }
            else
            {
                Progressbar.visibility=View.GONE

            }

        })
        VM.storecategory(storeCategoryListRequest)
    }
}


