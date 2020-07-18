package com.project.kaamwaala.fragment.admin.adminstall


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.admin.InventoryActivity
import com.project.kaamwaala.adapter.admin.stall.AdminStallListAdapter
import com.project.kaamwaala.dialog.admin.CreateStallDialog
import com.project.kaamwaala.model.admin_stall_detail.AdminStallDetailRequest
import com.project.kaamwaala.model.admin_stall_detail.AdminStallDetailResponse
import com.project.kaamwaala.model.adminstalllist.AdminStallListRequest
import com.project.kaamwaala.model.adminstalllist.AdminStallListResponse
import com.project.kaamwaala.model.adminstalllist.StoreItem
import com.project.kaamwaala.model.createstall.CreateStallRequest
import com.project.kaamwaala.model.createstall.CreateStallResponse
import com.project.kaamwaala.network.PrefsConstants
import com.project.kaamwaala.OnItemClickCreateStall
import com.project.kaamwaala.OnItemClickStall
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_admin_stall.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class AdminStallFragment: Fragment(), KodeinAware, OnItemClickStall, OnItemClickCreateStall {
    override val kodein by kodein()
    private val factory: AdminStallVMF by instance<AdminStallVMF>()
    private lateinit var VM: AdminStallVM
    var listLive: MutableList<StoreItem> = mutableListOf()
    var listOpening: MutableList<StoreItem> = mutableListOf()
    var list: MutableList<String> = mutableListOf()

    lateinit var callback: OnItemClickStall
    lateinit var callback1: OnItemClickCreateStall

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View=inflater.inflate(R.layout.fragment_admin_stall, container, false)

        VM = ViewModelProviders.of(this, factory).get(AdminStallVM::class.java)

        callback=this
        callback1=this

        Paper.init(activity)

        return  view

    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cardviewStore.setOnClickListener { createstallDialog() }

        ivAdd.setOnClickListener { createstallDialog() }

        StallListApi(AdminStallListRequest(Prefs.getString(PrefsConstants.UserId,""),"","0"))
    }


    private fun createstallDialog() {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        val newFragment = CreateStallDialog()
        newFragment.show(ft, "dialog")
        newFragment.setCallBack(callback1)
    }


        //=====================================Admin================================================//
    private fun StallListApi(adminStallListRequest: AdminStallListRequest) {
        VM?.responseStallList = MutableLiveData()
        VM?.responseStallList?.observe(this, Observer {
            progressbar.visibility=View.GONE
            val response = ( it as AdminStallListResponse)
            if(response.Status)
            {
                rlCreate.visibility=View.GONE
                rlStall.visibility=View.VISIBLE
                //startActivity(Intent(dialog!!.context,InventoryActivity::class.java))

                listOpening.clear()
                listLive.clear()

                for (i in 0 until response.StoreListItem.size) {
                    if(response.StoreListItem.get(i).isApproved=="False")
                    {
                        listOpening.add(response.StoreListItem[i])
                    }
                    else
                    {
                        listLive.add(response.StoreListItem[i])
                    }
                }



                rvLiveStall.apply {
                    rvLiveStall.layoutManager = LinearLayoutManager(context)
                    adapter = AdminStallListAdapter(listLive,callback) }

                rvOpeningStall.apply {
                    rvOpeningStall.layoutManager =LinearLayoutManager(context)
                    adapter = AdminStallListAdapter(listOpening,callback) }

            }
            else
            {
                progressbar.visibility=View.GONE
                rlCreate.visibility=View.VISIBLE
                rlStall.visibility=View.GONE
            }

        })
        VM.stalllist(adminStallListRequest)
    }

    override fun onClickStall(storeid: String) {
        Progressbar.visibility=View.VISIBLE
        StallDetailApi(AdminStallDetailRequest(Prefs.getString(PrefsConstants.UserId,""),storeid,"0"))
    }

    override fun onClickCreateStall(storename: String) {
        Progressbar.visibility=View.VISIBLE
        CreateStallApi(CreateStallRequest("","","","",0,"",0,"",""
            ,"","","","","","","" ,
            "",false,0,"Insert",0,"",0,"","",
            "","","","","","",0,
            "",storename,"","", Prefs.getString(PrefsConstants.UserId,""),"",""
            ,""))
    }

    //==========================================API===================================================//
    private fun CreateStallApi(createStallRequest:CreateStallRequest) {
        VM?.responseStall = MutableLiveData()
        VM?.responseStall?.observe(this, Observer {

            val response = ( it as CreateStallResponse)
            if(response.Status)
            {
                StallListApi(AdminStallListRequest(Prefs.getString(PrefsConstants.UserId,""),"","0"))
            }
            else
            {
                Progressbar.visibility=View.GONE
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        })
        VM.createstall(createStallRequest)
    }

    private fun StallDetailApi(adminStallDetailRequest: AdminStallDetailRequest) {
        VM?.responseStallDetail = MutableLiveData()
        VM?.responseStallDetail?.observe(this, Observer {


            val response = ( it as AdminStallDetailResponse)
            if(response.Status)
            {
                Paper.book().delete("stalldetail")

                Paper.book()
                    .write("stalldetail", response)

                Progressbar.visibility=View.GONE
                startActivity(
                    Intent( activity,
                        InventoryActivity::class.java))
            }
            else
            {


            }


        })
        VM.stalldetail(adminStallDetailRequest)
    }

}


